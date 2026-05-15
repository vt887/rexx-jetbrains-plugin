package org.rexxlang.intellij.rexx

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
