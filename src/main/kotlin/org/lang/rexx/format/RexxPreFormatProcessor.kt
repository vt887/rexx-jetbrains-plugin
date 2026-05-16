package org.lang.rexx.format

import com.intellij.application.options.CodeStyle
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.impl.source.codeStyle.PreFormatProcessor
import org.lang.rexx.RexxLanguage

internal const val REXX_FIRST_LINE_COMMENT = "/* The first line of a REXX exec must always be a comment. */"

// ---------------------------------------------------------------------------
// First-line comment rule
// ---------------------------------------------------------------------------

internal fun needsRexxFirstLineComment(text: String): Boolean =
    text
        .lineSequence()
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

internal fun reformatIndentation(
    text: String,
    indentSize: Int = 4,
): String {
    val unit = " ".repeat(indentSize)
    val lines = text.lines()
    val out = mutableListOf<String>()
    var level = 0
    var extraOneShot = 0 // temporary extra indent for next printable line only
    var inBlockComment = false

    for (raw in lines) {
        val normalizedLine = normalizeWhitespace(raw, indentSize)
        val trimmed = normalizedLine.trim()

        // Rule 3.4: collapse consecutive blank lines to at most one
        if (trimmed.isEmpty()) {
            if (out.lastOrNull() != "") out.add("")
            continue
        }

        // Inside a multi-line block comment: keep indent, watch for closing */
        if (inBlockComment) {
            out.add(unit.repeat(level + extraOneShot) + trimmed)
            if (trimmed.contains("*/")) inBlockComment = false
            continue
        }

        // Single-line or opening block comment: indent at current level but don't consume extraOneShot
        if (trimmed.startsWith("/*")) {
            out.add(unit.repeat(level + extraOneShot) + trimmed)
            if (!trimmed.contains("*/")) inBlockComment = true
            continue
        }

        val firstKw = firstEffectiveKeyword(trimmed)
        val isLabel = isLabelLine(trimmed)

        // Decrease indent before END (Rule 2.2)
        if (firstKw == "END" && level > 0) level--

        // Rule 7.1: labels always at column 0; other lines use level + one-shot
        val effectiveLevel = if (isLabel) 0 else (level + extraOneShot)
        extraOneShot = 0

        out.add(unit.repeat(effectiveLevel) + trimmed)

        // Update indent state after printing
        when {
            // Rule 7.1 / 8.1: label opens a procedure body
            isLabel -> level = 1

            // Standalone DO or DO <expr> (Rule 2.2 / 2.3)
            firstKw == "DO" -> level++

            // SELECT block (Rule 2.6)
            firstKw == "SELECT" -> level++

            // Trailing DO on any line: IF/THEN DO, ELSE DO, WHEN/THEN DO (Rules 2.4–2.6)
            lineEndsWithDo(trimmed) -> level++

            // Trailing THEN without DO: body is the next single statement (Rules 2.4 / 2.6)
            lineEndsWithThen(trimmed) -> extraOneShot = 1

            // OTHERWISE with no inline body: body is the next single statement (Rule 2.6)
            firstKw == "OTHERWISE" && noInlineBody(trimmed, "OTHERWISE") -> extraOneShot = 1

            // ELSE alone: body is the next single statement (Rule 2.5)
            firstKw == "ELSE" && !lineEndsWithDo(trimmed) && !lineEndsWithThen(trimmed) &&
                noInlineBody(trimmed, "ELSE") -> extraOneShot = 1

            // RETURN / EXIT close the label-opened procedure block (Rule 8.1)
            (firstKw == "RETURN" || firstKw == "EXIT") && level > 0 -> level--
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

/** True when the last meaningful token on the line is THEN (and not DO). */
private fun lineEndsWithThen(line: String): Boolean {
    val noComment = line.substringBefore("/*").trim()
    return noComment.split(Regex("\\s+")).lastOrNull()?.uppercase() == "THEN"
}

/** True when there is no body content after [keyword] on the line (ignores inline comments). */
private fun noInlineBody(
    line: String,
    keyword: String,
): Boolean {
    val noComment = line.substringBefore("/*").trim()
    return noComment
        .uppercase()
        .substringAfter(keyword)
        .trim()
        .isEmpty()
}

/** True when the line is a pure label: one identifier ending with ':', no spaces. */
private fun isLabelLine(line: String): Boolean {
    val t = line.trim()
    return t.endsWith(":") && !t.startsWith("/*") && !t.any { it.isWhitespace() } && t.length > 1
}

// ---------------------------------------------------------------------------
// Full document reformat: first-line comment + indentation
// ---------------------------------------------------------------------------

internal fun reformatDocument(
    text: String,
    indentSize: Int = 4,
): String =
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

private fun String.isSingleLineBlockComment(): Boolean = startsWith("/*") && endsWith("*/")

/** Rule 1.3: every file ends with exactly one trailing newline. */
private fun normalizeFileEnding(text: String): String {
    val withoutTrailingBlanks = text.replace(Regex("(?:\\R[\\t ]*)+\\z"), "")
    return withoutTrailingBlanks + "\n"
}

private fun normalizeWhitespace(
    line: String,
    indentSize: Int,
): String {
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
    override fun process(
        element: ASTNode,
        range: TextRange,
    ): TextRange {
        val file = element.psi.containingFile ?: return range
        if (!file.language.isKindOf(RexxLanguage) &&
            !file.viewProvider.baseLanguage.isKindOf(RexxLanguage)
        ) {
            return range
        }

        val documentManager = PsiDocumentManager.getInstance(file.project)
        val document = documentManager.getDocument(file) ?: return range

        val original = document.text
        val settings = CodeStyle.getSettings(file)
        val indentOptions = settings.getIndentOptions(file.fileType)
        val configuredIndent = indentOptions?.INDENT_SIZE ?: 4
        val indentSize = configuredIndent.coerceAtLeast(1)
        val reformatted = reformatDocument(original, indentSize)
        if (reformatted == original) return range

        document.replaceString(0, original.length, reformatted)
        documentManager.commitDocument(document)
        return TextRange(0, reformatted.length)
    }
}
