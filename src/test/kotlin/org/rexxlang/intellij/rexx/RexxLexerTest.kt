package org.rexxlang.intellij.rexx

import com.intellij.psi.tree.IElementType
import kotlin.test.Test
import kotlin.test.assertEquals

class RexxLexerTest {
    @Test
    fun tokenizesControlFlowCaseInsensitively() {
        val tokens = tokenTypes("do while wordin \\= ''\n  say wordin\nend")

        assertEquals(
            listOf(
                RexxTokenTypes.KEYWORD,
                RexxTokenTypes.KEYWORD,
                RexxTokenTypes.IDENTIFIER,
                RexxTokenTypes.OPERATOR,
                RexxTokenTypes.STRING,
                RexxTokenTypes.KEYWORD,
                RexxTokenTypes.IDENTIFIER,
                RexxTokenTypes.KEYWORD,
            ),
            tokens,
        )
    }

    @Test
    fun tokenizesNestedCommentsAndEscapedStrings() {
        val tokens = tokenTypes("say 'it''s ok' /* outer /* inner */ done */")

        assertEquals(
            listOf(
                RexxTokenTypes.KEYWORD,
                RexxTokenTypes.STRING,
                RexxTokenTypes.COMMENT,
            ),
            tokens,
        )
    }

    @Test
    fun tokenizesNumbersAndCompoundSymbols() {
        val tokens = tokenTypes("stem.1 = 42.5E+2")

        assertEquals(
            listOf(
                RexxTokenTypes.IDENTIFIER,
                RexxTokenTypes.OPERATOR,
                RexxTokenTypes.NUMBER,
            ),
            tokens,
        )
    }

    @Test
    fun tokenizesParseTemplateDotAsPunctuation() {
        val tokens = tokenTypes("parse pull wordin .")

        assertEquals(
            listOf(
                RexxTokenTypes.KEYWORD,
                RexxTokenTypes.KEYWORD,
                RexxTokenTypes.IDENTIFIER,
                RexxTokenTypes.PUNCTUATION,
            ),
            tokens,
        )
    }

    @Test
    fun keepsUnclosedStringAsStringToken() {
        val tokens = tokenTypes("say 'unclosed")

        assertEquals(
            listOf(
                RexxTokenTypes.KEYWORD,
                RexxTokenTypes.STRING,
            ),
            tokens,
        )
    }

    private fun tokenTypes(code: String): List<IElementType> {
        val lexer = RexxLexer()
        val tokens = mutableListOf<IElementType>()
        lexer.start(code)

        while (lexer.tokenType != null) {
            val tokenType = lexer.tokenType
            if (tokenType != null && tokenType != com.intellij.psi.TokenType.WHITE_SPACE) {
                tokens += tokenType
            }
            lexer.advance()
        }

        return tokens
    }
}
