package org.lang.rexx.format

import com.intellij.formatting.Alignment
import com.intellij.formatting.Block
import com.intellij.formatting.ChildAttributes
import com.intellij.formatting.FormattingContext
import com.intellij.formatting.FormattingModel
import com.intellij.formatting.FormattingModelBuilder
import com.intellij.formatting.FormattingModelProvider
import com.intellij.formatting.Indent
import com.intellij.formatting.Spacing
import com.intellij.formatting.Wrap
import com.intellij.lang.ASTNode

class RexxFormattingModelBuilder : FormattingModelBuilder {
    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val psiFile = formattingContext.psiElement.containingFile
        val rootNode = requireNotNull(psiFile.node ?: formattingContext.psiElement.node)

        return FormattingModelProvider.createFormattingModelForPsiFile(
            psiFile,
            RexxFormattingBlock(rootNode),
            formattingContext.codeStyleSettings,
        )
    }
}

private class RexxFormattingBlock(
    private val node: ASTNode,
) : Block {
    private val alignment = Alignment.createAlignment()

    override fun getTextRange() = node.textRange

    override fun getSubBlocks(): List<Block> = emptyList()

    override fun getWrap(): Wrap? = null

    override fun getIndent(): Indent = Indent.getNormalIndent()

    override fun getAlignment(): Alignment = alignment

    override fun getSpacing(
        child1: Block?,
        child2: Block,
    ): Spacing? = null

    override fun getChildAttributes(newChildIndex: Int): ChildAttributes = ChildAttributes(Indent.getNormalIndent(), alignment)

    override fun isIncomplete(): Boolean = false

    override fun isLeaf(): Boolean = true
}
