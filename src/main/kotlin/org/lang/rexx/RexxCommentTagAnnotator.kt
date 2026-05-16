package org.lang.rexx

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement

private val COMMENT_TAG_PATTERN = Regex("(?i)\\b(TODO|FIXME|NOTE)\\b")

internal fun findCommentTagRanges(text: String): List<IntRange> = COMMENT_TAG_PATTERN.findAll(text).map { it.range }.toList()

internal fun findCommentDelimiterRanges(text: String): List<IntRange> {
    if (!text.startsWith("/*")) return emptyList()
    val ranges = mutableListOf(0..1)
    if (text.endsWith("*/") && text.length >= 4) {
        ranges += (text.length - 2)..(text.length - 1)
    }
    return ranges
}

class RexxCommentTagAnnotator : Annotator {
    override fun annotate(
        element: PsiElement,
        holder: AnnotationHolder,
    ) {
        if (element.node.elementType != RexxTokenTypes.COMMENT) {
            return
        }

        val startOffset = element.textRange.startOffset
        for (matchRange in findCommentTagRanges(element.text)) {
            holder
                .newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(TextRange(startOffset + matchRange.first, startOffset + matchRange.last + 1))
                .textAttributes(RexxTextAttributes.COMMENT_TAG)
                .create()
        }

        for (delimiterRange in findCommentDelimiterRanges(element.text)) {
            holder
                .newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(TextRange(startOffset + delimiterRange.first, startOffset + delimiterRange.last + 1))
                .textAttributes(RexxTextAttributes.COMMENT)
                .create()
        }
    }
}
