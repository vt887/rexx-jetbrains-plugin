package org.lang.rexx.rexxlint

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.ExternalAnnotator
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile
import org.lang.rexx.RexxLanguage
import kotlin.math.max
import kotlin.math.min

data class RexxlintAnnotationInput(
    val source: String,
    val filePath: String?,
)

class RexxlintExternalAnnotator : ExternalAnnotator<RexxlintAnnotationInput, RexxlintCommandResult<List<RexxlintDiagnostic>>>() {
    override fun collectInformation(file: PsiFile): RexxlintAnnotationInput? {
        if (!file.language.isKindOf(RexxLanguage) && !file.viewProvider.baseLanguage.isKindOf(RexxLanguage)) {
            return null
        }
        return RexxlintAnnotationInput(file.text, file.virtualFile?.path)
    }

    override fun doAnnotate(collectedInfo: RexxlintAnnotationInput): RexxlintCommandResult<List<RexxlintDiagnostic>> =
        service<RexxlintBridgeService>().lint(collectedInfo.source, collectedInfo.filePath)

    override fun apply(
        file: PsiFile,
        annotationResult: RexxlintCommandResult<List<RexxlintDiagnostic>>?,
        holder: AnnotationHolder,
    ) {
        when (annotationResult) {
            null -> Unit
            is RexxlintCommandResult.Success -> annotationResult.value.forEach { diagnostic ->
                val range = diagnostic.toTextRange(file.viewProvider.document ?: return@forEach)
                val description = diagnostic.code?.let { "${diagnostic.message} [$it]" } ?: diagnostic.message
                holder.newAnnotation(diagnostic.severity, description).range(range).create()
            }
            is RexxlintCommandResult.Failure -> {
                holder.newAnnotation(com.intellij.lang.annotation.HighlightSeverity.WARNING, annotationResult.message)
                    .fileLevel()
                    .create()
            }
        }
    }
}

internal fun RexxlintDiagnostic.toTextRange(document: Document): TextRange {
    val start = offsetFor(document, line, column)
    val end = offsetFor(document, endLine ?: line, endColumn ?: (column + 1))
    return TextRange(start, max(start + 1, end))
}

private fun offsetFor(
    document: Document,
    line: Int,
    column: Int,
): Int {
    val safeLine = min(max(line, 1), max(document.lineCount, 1)) - 1
    val lineStart = document.getLineStartOffset(safeLine)
    val lineEnd = document.getLineEndOffset(safeLine)
    val safeColumn = max(column, 1) - 1
    return min(lineStart + safeColumn, lineEnd)
}
