package org.lang.rexx.format

import com.intellij.lang.ASTNode
import com.intellij.openapi.components.service
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.impl.source.codeStyle.PreFormatProcessor
import org.lang.rexx.RexxLanguage
import org.lang.rexx.rexxlint.RexxlintBridgeService
import org.lang.rexx.rexxlint.RexxlintNotifier

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
        val bridge = service<RexxlintBridgeService>()
        return when (val result = bridge.format(original, file.virtualFile?.path)) {
            is org.lang.rexx.rexxlint.RexxlintCommandResult.Success -> {
                if (result.value == original) {
                    range
                } else {
                    document.replaceString(0, original.length, result.value)
                    documentManager.commitDocument(document)
                    TextRange(0, result.value.length)
                }
            }

            is org.lang.rexx.rexxlint.RexxlintCommandResult.Failure -> {
                RexxlintNotifier.notifyFormattingFailure(file.project, result.message)
                range
            }
        }
    }
}
