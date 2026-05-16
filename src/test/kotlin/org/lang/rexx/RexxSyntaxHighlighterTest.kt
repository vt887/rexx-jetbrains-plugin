package org.lang.rexx

import com.intellij.psi.TokenType
import kotlin.test.Test
import kotlin.test.assertEquals

class RexxSyntaxHighlighterTest {
    private val highlighter = RexxSyntaxHighlighter()

    @Test
    fun mapsCommentToken() {
        assertEquals(RexxTextAttributes.COMMENT, keyFor(RexxTokenTypes.COMMENT))
    }

    @Test
    fun mapsKeywordToken() {
        assertEquals(RexxTextAttributes.KEYWORD, keyFor(RexxTokenTypes.KEYWORD))
    }

    @Test
    fun mapsIdentifierToken() {
        assertEquals(RexxTextAttributes.VARIABLE, keyFor(RexxTokenTypes.IDENTIFIER))
    }

    @Test
    fun mapsFunctionCallToken() {
        assertEquals(RexxTextAttributes.FUNCTION, keyFor(RexxTokenTypes.FUNCTION_CALL))
    }

    @Test
    fun mapsBuiltinFunctionToken() {
        assertEquals(RexxTextAttributes.BUILTIN_FUNCTION, keyFor(RexxTokenTypes.BUILTIN_FUNCTION))
    }

    @Test
    fun mapsLabelToken() {
        assertEquals(RexxTextAttributes.LABEL, keyFor(RexxTokenTypes.LABEL))
    }

    @Test
    fun mapsStringToken() {
        assertEquals(RexxTextAttributes.STRING, keyFor(RexxTokenTypes.STRING))
    }

    @Test
    fun mapsNumberToken() {
        assertEquals(RexxTextAttributes.NUMBER, keyFor(RexxTokenTypes.NUMBER))
    }

    @Test
    fun mapsOperatorToken() {
        assertEquals(RexxTextAttributes.OPERATOR, keyFor(RexxTokenTypes.OPERATOR))
    }

    @Test
    fun mapsAssignmentOperatorToken() {
        assertEquals(RexxTextAttributes.ASSIGNMENT_OPERATOR, keyFor(RexxTokenTypes.ASSIGNMENT_OPERATOR))
    }

    @Test
    fun mapsComparisonOperatorToken() {
        assertEquals(RexxTextAttributes.COMPARISON_OPERATOR, keyFor(RexxTokenTypes.COMPARISON_OPERATOR))
    }

    @Test
    fun mapsLogicalOperatorToken() {
        assertEquals(RexxTextAttributes.LOGICAL_OPERATOR, keyFor(RexxTokenTypes.LOGICAL_OPERATOR))
    }

    @Test
    fun mapsArithmeticOperatorToken() {
        assertEquals(RexxTextAttributes.ARITHMETIC_OPERATOR, keyFor(RexxTokenTypes.ARITHMETIC_OPERATOR))
    }

    @Test
    fun mapsPunctuationToken() {
        assertEquals(RexxTextAttributes.PUNCTUATION, keyFor(RexxTokenTypes.PUNCTUATION))
    }

    @Test
    fun mapsBadCharacterToken() {
        assertEquals(RexxTextAttributes.BAD_CHARACTER, keyFor(TokenType.BAD_CHARACTER))
    }

    private fun keyFor(tokenType: com.intellij.psi.tree.IElementType) =
        highlighter.getTokenHighlights(tokenType).single()
}
