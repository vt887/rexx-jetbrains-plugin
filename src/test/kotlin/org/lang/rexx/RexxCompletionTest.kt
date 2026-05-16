package org.lang.rexx

import com.intellij.codeInsight.lookup.LookupElementPresentation
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RexxCompletionTest : BasePlatformTestCase() {
    fun testExposesRequestedKeywordSet() {
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

    fun testRendersImportantKeywordsWithBoldDescriptions() {
        val presentation = LookupElementPresentation()

        rexxKeywordLookupElements().first { it.lookupString == "DO" }.renderElement(presentation)

        assertEquals("DO", presentation.itemText)
        assertEquals(" ... END", presentation.tailText)
        assertTrue(presentation.isItemTextBold)
    }

    fun testSkipsCompletionInsideCommentsAndStrings() {
        assertFalse(isRexxKeywordCompletionAllowed(RexxLanguage, RexxTokenTypes.COMMENT))
        assertFalse(isRexxKeywordCompletionAllowed(RexxLanguage, RexxTokenTypes.STRING))
        assertTrue(isRexxKeywordCompletionAllowed(RexxLanguage, RexxTokenTypes.IDENTIFIER))
    }
}
