package org.lang.rexx

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet

class RexxParserDefinition : ParserDefinition {
    override fun createLexer(project: Project?): Lexer = RexxLexerAdapter()

    override fun createParser(project: Project?): PsiParser = RexxPsiParser

    override fun getFileNodeType(): IFileElementType = FILE

    override fun getCommentTokens(): TokenSet = RexxTokenSets.COMMENTS

    override fun getStringLiteralElements(): TokenSet = RexxTokenSets.STRINGS

    override fun createElement(node: ASTNode): PsiElement = RexxPsiElement(node)

    override fun createFile(viewProvider: FileViewProvider): PsiFile = RexxFile(viewProvider)

    override fun getWhitespaceTokens(): TokenSet = TokenSet.create(TokenType.WHITE_SPACE)

    override fun spaceExistenceTypeBetweenTokens(
        left: ASTNode,
        right: ASTNode,
    ): ParserDefinition.SpaceRequirements = ParserDefinition.SpaceRequirements.MAY

    private companion object {
        private val FILE = IFileElementType(RexxLanguage)
    }
}
