package org.lang.rexx

import com.intellij.psi.TokenType
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import kotlin.test.assertEquals

class RexxSyntaxHighlighterTest : BasePlatformTestCase() {
    private lateinit var highlighter: RexxSyntaxHighlighter

    override fun setUp() {
        super.setUp()
        highlighter = RexxSyntaxHighlighter()
    }

    fun testMapsCommentToken() {
        assertEquals(RexxTextAttributes.COMMENT, keyFor(RexxTokenTypes.COMMENT))
    }

    fun testMapsKeywordToken() {
        assertEquals(RexxTextAttributes.KEYWORD, keyFor(RexxTokenTypes.KEYWORD))
    }

    fun testMapsIdentifierToken() {
        assertEquals(RexxTextAttributes.VARIABLE, keyFor(RexxTokenTypes.IDENTIFIER))
    }

    fun testMapsFunctionCallToken() {
        assertEquals(RexxTextAttributes.FUNCTION, keyFor(RexxTokenTypes.FUNCTION_CALL))
    }

    fun testMapsBuiltinFunctionToken() {
        assertEquals(RexxTextAttributes.BUILTIN_FUNCTION, keyFor(RexxTokenTypes.BUILTIN_FUNCTION))
    }

    fun testMapsLabelToken() {
        assertEquals(RexxTextAttributes.LABEL, keyFor(RexxTokenTypes.LABEL))
    }

    fun testMapsStringToken() {
        assertEquals(RexxTextAttributes.STRING, keyFor(RexxTokenTypes.STRING))
    }

    fun testMapsNumberToken() {
        assertEquals(RexxTextAttributes.NUMBER, keyFor(RexxTokenTypes.NUMBER))
    }

    fun testMapsOperatorToken() {
        assertEquals(RexxTextAttributes.OPERATOR, keyFor(RexxTokenTypes.OPERATOR))
    }

    fun testMapsAssignmentOperatorToken() {
        assertEquals(RexxTextAttributes.ASSIGNMENT_OPERATOR, keyFor(RexxTokenTypes.ASSIGNMENT_OPERATOR))
    }

    fun testMapsComparisonOperatorToken() {
        assertEquals(RexxTextAttributes.COMPARISON_OPERATOR, keyFor(RexxTokenTypes.COMPARISON_OPERATOR))
    }

    fun testMapsLogicalOperatorToken() {
        assertEquals(RexxTextAttributes.LOGICAL_OPERATOR, keyFor(RexxTokenTypes.LOGICAL_OPERATOR))
    }

    fun testMapsArithmeticOperatorToken() {
        assertEquals(RexxTextAttributes.ARITHMETIC_OPERATOR, keyFor(RexxTokenTypes.ARITHMETIC_OPERATOR))
    }

    fun testMapsPunctuationToken() {
        assertEquals(RexxTextAttributes.PUNCTUATION, keyFor(RexxTokenTypes.PUNCTUATION))
    }

    fun testMapsBadCharacterToken() {
        assertEquals(RexxTextAttributes.BAD_CHARACTER, keyFor(TokenType.BAD_CHARACTER))
    }

    private fun keyFor(tokenType: com.intellij.psi.tree.IElementType) =
        highlighter.getTokenHighlights(tokenType).single()
}
