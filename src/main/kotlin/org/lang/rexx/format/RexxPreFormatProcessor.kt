package org.lang.rexx.format

import com.intellij.lang.ASTNode
import com.intellij.application.options.CodeStyle
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.impl.source.codeStyle.PreFormatProcessor
import org.lang.rexx.RexxLanguage

internal const val REXX_FIRST_LINE_COMMENT = "/* The first line of a REXX exec must always be a comment. */"

// ---------------------------------------------------------------------------
// First-line comment rule
// ---------------------------------------------------------------------------

internal fun needsRexxFirstLineComment(text: String): Boolean =
    text.lineSequence()
        .firstOrNull { it.isNotBlank() }
        ?.trimStart()
        ?.startsWith("/*")
        ?.not()
        ?: true

internal fun ensureRexxFirstLineComment(text: String): String {
    if (!needsRexxFirstLineComment(text)) return text
    val prefix = if (text.isEmpty()) REXX_FIRST_LINE_COMMENT else "$REXX_FIRST_LINE_COMMENT\n"
    return prefix + text
}

// ---------------------------------------------------------------------------
// Indentation reformatter
// Per Rexx Style Guide: indent DO/SELECT blocks 1-3 spaces (default 3 for this plugin).
// https://erroneousbee.github.io/Computers/RexxStyleGuide.html
// ---------------------------------------------------------------------------

internal fun reformatIndentation(text: String, indentSize: Int = 3): String {
    val unit = " ".repeat(indentSize)
    val lines = text.lines()
    val out = mutableListOf<String>()
    var level = 0
    var inBlockComment = false

    for (raw in lines) {
        val normalizedLine = normalizeWhitespace(raw, indentSize)
        val trimmed = normalizedLine.trim()

        if (trimmed.isEmpty()) {
            out.add("")
            continue
        }

        // Inside a multi-line block comment: preserve content, keep indent, watch for close
        if (inBlockComment) {
            out.add(unit.repeat(level) + trimmed)
            if (trimmed.contains("*/")) inBlockComment = false
            continue
        }

        // Line that opens a block comment
        if (trimmed.startsWith("/*")) {
            out.add(unit.repeat(level) + trimmed)
            if (!trimmed.contains("*/")) inBlockComment = true
            continue
        }

        val firstKw = firstEffectiveKeyword(trimmed)

        // Decrease indent BEFORE printing these keywords
        when (firstKw) {
            "END" -> if (level > 0) level--
        }

        out.add(unit.repeat(level) + trimmed)

        // Increase indent AFTER printing these keywords
        when {
            firstKw == "DO" -> level++
            firstKw == "SELECT" -> level++
            // handles "IF cond THEN DO" or "ELSE DO" at end of line
            lineEndsWithDo(trimmed) -> level++
        }
    }

    return out.joinToString("\n")
}

/** Returns the first Rexx keyword on the line, uppercased.
 *  Skips an optional label prefix (e.g. "MyProc:"). */
private fun firstEffectiveKeyword(line: String): String {
    var rest = line.trim()
    // Skip label prefix: word followed immediately by ':'
    val colonIdx = rest.indexOf(':')
    if (colonIdx > 0 && colonIdx < rest.length - 1) {
        val candidate = rest.substring(0, colonIdx)
        if (candidate.all { it.isLetterOrDigit() || it in "._!?@#\$" }) {
            rest = rest.substring(colonIdx + 1).trim()
        }
    }
    return rest.split(Regex("\\s+")).firstOrNull()?.uppercase() ?: ""
}

/** Returns true when the meaningful last token of the line is DO,
 *  e.g. "IF x > 0 THEN DO" or "ELSE DO". Ignores trailing inline comment. */
private fun lineEndsWithDo(line: String): Boolean {
    val noComment = line.substringBefore("/*").trim()
    return noComment.split(Regex("\\s+")).lastOrNull()?.uppercase() == "DO"
}

// ---------------------------------------------------------------------------
// Full document reformat: first-line comment + indentation
// ---------------------------------------------------------------------------

internal fun reformatDocument(text: String, indentSize: Int = 3): String =
    normalizeFileEnding(
        reformatIndentation(
            mergeEmptyLineBetweenSingleLineBlockComments(ensureRexxFirstLineComment(text)),
            indentSize,
        ),
    )

private fun mergeEmptyLineBetweenSingleLineBlockComments(text: String): String {
    val lines = text.lines()
    if (lines.size < 3) return text

    val out = mutableListOf<String>()
    var i = 0
    while (i < lines.size) {
        val current = lines[i]
        out += current

        if (current.trim().isSingleLineBlockComment()) {
            var j = i + 1
            while (j < lines.size && lines[j].isBlank()) {
                j++
            }

            val blankCount = j - (i + 1)
            if (blankCount > 0 &&
                j < lines.size &&
                lines[j].trim().isSingleLineBlockComment()
            ) {
                if (blankCount >= 2) {
                    out += ""
                }
                i = j
                continue
            }
        }
        i++
    }
    return out.joinToString("\n")
}

private fun String.isSingleLineBlockComment(): Boolean =
    startsWith("/*") && endsWith("*/")

private fun normalizeFileEnding(text: String): String {
    val withoutTrailingBlankLines = text.replace(Regex("(?:\\R[\\t ]*)+\\z"), "")
    return withoutTrailingBlankLines + "\n\n"
}

private fun normalizeWhitespace(line: String, indentSize: Int): String {
    val spaces = " ".repeat(indentSize)
    val expandedTabs = line.replace("\t", spaces)
    val withoutTrailing = expandedTabs.trimEnd()
    return normalizeCommentSpacing(withoutTrailing)
}

private fun normalizeCommentSpacing(line: String): String {
    val commentStart = line.indexOf("/*")
    if (commentStart < 0) return line

    val prefix = line.substring(0, commentStart)
    val comment = line.substring(commentStart)
    val compactComment = comment.replace(Regex(" {2,}"), " ")
    return prefix + compactComment
}

// ---------------------------------------------------------------------------
// PreFormatProcessor
// ---------------------------------------------------------------------------

class RexxPreFormatProcessor : PreFormatProcessor {
    override fun process(element: ASTNode, range: TextRange): TextRange {
        val file = element.psi.containingFile ?: return range
        if (!file.language.isKindOf(RexxLanguage) &&
            !file.viewProvider.baseLanguage.isKindOf(RexxLanguage)
        ) return range

        val documentManager = PsiDocumentManager.getInstance(file.project)
        val document = documentManager.getDocument(file) ?: return range

        val original = document.text
        val settings = CodeStyle.getSettings(file)
        val indentOptions = settings.getIndentOptions(file.fileType)
        val configuredIndent = indentOptions?.INDENT_SIZE ?: 3
        val indentSize = configuredIndent.coerceIn(1, 3)
        val reformatted = reformatDocument(original, indentSize)
        if (reformatted == original) return range

        document.replaceString(0, original.length, reformatted)
        documentManager.commitDocument(document)
        return TextRange(0, reformatted.length)
    }
}
