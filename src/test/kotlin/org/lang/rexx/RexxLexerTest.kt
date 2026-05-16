package org.lang.rexx

import com.intellij.psi.tree.IElementType
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import kotlin.test.assertEquals

class RexxLexerTest : BasePlatformTestCase() {
    fun testTokenizesControlFlowCaseInsensitively() {
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

    fun testTokenizesNestedCommentsAndEscapedStrings() {
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

    fun testTokenizesNumbersAndCompoundSymbols() {
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

    fun testTokenizesParseTemplateDotAsPunctuation() {
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

    fun testKeepsUnclosedStringAsStringToken() {
        val tokens = tokenTypes("say 'unclosed")

        assertEquals(
            listOf(
                RexxTokenTypes.KEYWORD,
                RexxTokenTypes.STRING,
            ),
            tokens,
        )
    }

    fun testTokenizesFunctionBuiltinAndLabel() {
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
