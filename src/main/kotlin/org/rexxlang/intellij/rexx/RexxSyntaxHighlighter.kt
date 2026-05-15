package org.rexxlang.intellij.rexx

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
            RexxTokenTypes.STRING to RexxTextAttributes.STRING,
            RexxTokenTypes.NUMBER to RexxTextAttributes.NUMBER,
            RexxTokenTypes.OPERATOR to RexxTextAttributes.OPERATOR,
            RexxTokenTypes.PUNCTUATION to RexxTextAttributes.PUNCTUATION,
            TokenType.BAD_CHARACTER to RexxTextAttributes.BAD_CHARACTER,
        )
    }
}
