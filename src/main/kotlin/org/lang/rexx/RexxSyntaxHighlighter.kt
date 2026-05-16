package org.lang.rexx

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType

class RexxSyntaxHighlighter : SyntaxHighlighterBase() {
    override fun getHighlightingLexer(): Lexer = RexxLexerAdapter()

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> =
        pack(ATTRIBUTES[tokenType])

    private companion object {
        private val ATTRIBUTES = mapOf(
            RexxTokenTypes.COMMENT to RexxTextAttributes.COMMENT,
            RexxTokenTypes.KEYWORD to RexxTextAttributes.KEYWORD,
            RexxTokenTypes.IDENTIFIER to RexxTextAttributes.VARIABLE,
            RexxTokenTypes.FUNCTION_CALL to RexxTextAttributes.FUNCTION,
            RexxTokenTypes.BUILTIN_FUNCTION to RexxTextAttributes.BUILTIN_FUNCTION,
            RexxTokenTypes.LABEL to RexxTextAttributes.LABEL,
            RexxTokenTypes.STRING to RexxTextAttributes.STRING,
            RexxTokenTypes.NUMBER to RexxTextAttributes.NUMBER,
            RexxTokenTypes.OPERATOR to RexxTextAttributes.OPERATOR,
            RexxTokenTypes.ASSIGNMENT_OPERATOR to RexxTextAttributes.ASSIGNMENT_OPERATOR,
            RexxTokenTypes.COMPARISON_OPERATOR to RexxTextAttributes.COMPARISON_OPERATOR,
            RexxTokenTypes.LOGICAL_OPERATOR to RexxTextAttributes.LOGICAL_OPERATOR,
            RexxTokenTypes.ARITHMETIC_OPERATOR to RexxTextAttributes.ARITHMETIC_OPERATOR,
            RexxTokenTypes.PUNCTUATION to RexxTextAttributes.PUNCTUATION,
            TokenType.BAD_CHARACTER to RexxTextAttributes.BAD_CHARACTER,
        )
    }
}
