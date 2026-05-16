package org.rexxlang.intellij.rexx

import com.intellij.codeInsight.lookup.LookupElementPresentation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RexxCompletionTest {
    @Test
    fun exposesRequestedKeywordSet() {
        assertEquals(
            listOf(
                "ADDRESS",
                "ARG",
                "CALL",
                "DO",
                "DROP",
                "ELSE",
                "END",
                "EXIT",
                "IF",
                "INTERPRET",
                "ITERATE",
                "LEAVE",
                "NOP",
                "NUMERIC",
                "OTHERWISE",
                "PARSE",
                "PROCEDURE",
                "PULL",
                "PUSH",
                "QUEUE",
                "RETURN",
                "SAY",
                "SELECT",
                "SIGNAL",
                "THEN",
                "TRACE",
                "WHEN",
                "WHILE",
            ),
            rexxKeywordLookupElements().map { it.lookupString },
        )
    }

    @Test
    fun rendersImportantKeywordsWithBoldDescriptions() {
        val presentation = LookupElementPresentation()

        rexxKeywordLookupElements().first { it.lookupString == "DO" }.renderElement(presentation)

        assertEquals("DO", presentation.itemText)
        assertEquals(" ... END", presentation.tailText)
        assertTrue(presentation.isItemTextBold)
    }

    @Test
    fun skipsCompletionInsideCommentsAndStrings() {
        assertFalse(isRexxKeywordCompletionAllowed(RexxLanguage, RexxTokenTypes.COMMENT))
        assertFalse(isRexxKeywordCompletionAllowed(RexxLanguage, RexxTokenTypes.STRING))
        assertTrue(isRexxKeywordCompletionAllowed(RexxLanguage, RexxTokenTypes.IDENTIFIER))
    }
}
