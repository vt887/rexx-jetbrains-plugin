package org.lang.rexx

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
                RexxTokenTypes.COMPARISON_OPERATOR,
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
                RexxTokenTypes.ASSIGNMENT_OPERATOR,
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

    @Test
    fun tokenizesFunctionBuiltinAndLabel() {
        val tokens = tokenTypes("start: total = length(wordin) + customFn(x)")
        assertEquals(
            listOf(
                RexxTokenTypes.LABEL,
                RexxTokenTypes.IDENTIFIER,
                RexxTokenTypes.ASSIGNMENT_OPERATOR,
                RexxTokenTypes.BUILTIN_FUNCTION,
                RexxTokenTypes.PUNCTUATION,
                RexxTokenTypes.IDENTIFIER,
                RexxTokenTypes.PUNCTUATION,
                RexxTokenTypes.ARITHMETIC_OPERATOR,
                RexxTokenTypes.FUNCTION_CALL,
                RexxTokenTypes.PUNCTUATION,
                RexxTokenTypes.IDENTIFIER,
                RexxTokenTypes.PUNCTUATION,
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
