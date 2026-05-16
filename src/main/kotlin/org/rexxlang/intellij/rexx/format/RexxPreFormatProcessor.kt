package org.rexxlang.intellij.rexx.format

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.impl.source.codeStyle.PreFormatProcessor
import org.rexxlang.intellij.rexx.RexxLanguage

internal const val REXX_FIRST_LINE_COMMENT = "/* The first line of a REXX exec must always be a comment. */"

internal fun needsRexxFirstLineComment(text: String): Boolean =
    text.lineSequence()
        .firstOrNull { it.isNotBlank() }
        ?.trimStart()
        ?.startsWith("/*")
        ?.not()
        ?: true

internal fun ensureRexxFirstLineComment(text: String): String {
    if (!needsRexxFirstLineComment(text)) {
        return text
    }

    val prefix = if (text.isEmpty()) REXX_FIRST_LINE_COMMENT else "$REXX_FIRST_LINE_COMMENT\n"
    return prefix + text
}

class RexxPreFormatProcessor : PreFormatProcessor {
    override fun process(element: ASTNode, range: TextRange): TextRange {
        val file = element.psi.containingFile ?: return range
        if (!file.language.isKindOf(RexxLanguage) && !file.viewProvider.baseLanguage.isKindOf(RexxLanguage)) {
            return range
        }

        val documentManager = PsiDocumentManager.getInstance(file.project)
        val document = documentManager.getDocument(file) ?: return range
        val updatedText = ensureRexxFirstLineComment(document.text)
        if (updatedText == document.text) {
            return range
        }

        val insertedLength = updatedText.length - document.textLength
        document.insertString(0, updatedText.substring(0, insertedLength))
        documentManager.commitDocument(document)
        return TextRange(0, range.endOffset + insertedLength)
    }
}
