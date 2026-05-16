package org.rexxlang.intellij.rexx

import org.rexxlang.intellij.rexx.format.REXX_FIRST_LINE_COMMENT
import org.rexxlang.intellij.rexx.format.ensureRexxFirstLineComment
import org.rexxlang.intellij.rexx.format.needsRexxFirstLineComment
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RexxFormattingTest {
    @Test
    fun insertsMandatoryFirstLineCommentWhenMissing() {
        val source = "say 'hello'"

        assertEquals(
            "$REXX_FIRST_LINE_COMMENT\nsay 'hello'",
            ensureRexxFirstLineComment(source),
        )
    }

    @Test
    fun preservesExistingFirstNonEmptyLineComment() {
        val source = "\n  /* already present */\nsay 'hello'"

        assertEquals(source, ensureRexxFirstLineComment(source))
        assertFalse(needsRexxFirstLineComment(source))
    }

    @Test
    fun flagsNonCommentFirstExecutableLine() {
        assertTrue(needsRexxFirstLineComment("\n\naddress tty"))
    }
}
