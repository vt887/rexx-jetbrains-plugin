package org.lang.rexx

import org.lang.rexx.format.REXX_FIRST_LINE_COMMENT
import org.lang.rexx.format.ensureRexxFirstLineComment
import org.lang.rexx.format.needsRexxFirstLineComment
import org.lang.rexx.format.reformatDocument
import org.lang.rexx.format.reformatIndentation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RexxFormattingTest {
    // -----------------------------------------------------------------------
    // First-line comment rule
    // -----------------------------------------------------------------------

    @Test
    fun insertsMandatoryFirstLineCommentWhenMissing() {
        val source = "say 'hello'"
        assertEquals("$REXX_FIRST_LINE_COMMENT\nsay 'hello'", ensureRexxFirstLineComment(source))
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

    // -----------------------------------------------------------------------
    // Indentation: DO / END
    // -----------------------------------------------------------------------

    @Test
    fun indentsDoBlock() {
        val source = "do\nsay 'x'\nend"
        val expected = "do\n    say 'x'\nend"
        assertEquals(expected, reformatIndentation(source))
    }

    @Test
    fun indentsNestedDoBlocks() {
        val source = "do\ndo\nsay 'x'\nend\nend"
        val expected = "do\n    do\n        say 'x'\n    end\nend"
        assertEquals(expected, reformatIndentation(source))
    }

    @Test
    fun indentsSelectBlock() {
        val source = "select\nwhen x = 1 then\nsay 'one'\nend"
        val expected = "select\n    when x = 1 then\n        say 'one'\nend"
        assertEquals(expected, reformatIndentation(source))
    }

    @Test
    fun indentsOtherwiseAtSameLevelAsWhen() {
        val source = "select\nwhen x = 1 then\nsay 'one'\notherwise\nsay 'other'\nend"
        val expected = "select\n    when x = 1 then\n        say 'one'\n    otherwise\n        say 'other'\nend"
        assertEquals(expected, reformatIndentation(source))
    }

    @Test
    fun handlesThenDoAtEndOfLine() {
        val source = "if x > 0 then do\nsay 'positive'\nend"
        val expected = "if x > 0 then do\n    say 'positive'\nend"
        assertEquals(expected, reformatIndentation(source))
    }

    @Test
    fun preservesEmptyLines() {
        val source = "do\n\nsay 'x'\n\nend"
        val expected = "do\n\n    say 'x'\n\nend"
        assertEquals(expected, reformatIndentation(source))
    }

    @Test
    fun preservesBlockCommentLines() {
        val source = "do\n/* comment */\nsay 'x'\nend"
        val expected = "do\n    /* comment */\n    say 'x'\nend"
        assertEquals(expected, reformatIndentation(source))
    }

    @Test
    fun preservesMultiLineBlockComment() {
        val source = "do\n/* line1\nline2 */\nsay 'x'\nend"
        val expected = "do\n    /* line1\n    line2 */\n    say 'x'\nend"
        assertEquals(expected, reformatIndentation(source))
    }

    @Test
    fun customIndentSize() {
        val source = "do\nsay 'x'\nend"
        val expected = "do\n  say 'x'\nend"
        assertEquals(expected, reformatIndentation(source, indentSize = 2))
    }

    @Test
    fun indentsThenWithoutDo() {
        val source = "if x = 1 then\nsay 'one'"
        val expected = "if x = 1 then\n    say 'one'"
        assertEquals(expected, reformatIndentation(source))
    }

    @Test
    fun indentsIfThenElse() {
        val source = "if x = 1 then\nsay 'one'\nelse\nsay 'other'"
        val expected = "if x = 1 then\n    say 'one'\nelse\n    say 'other'"
        assertEquals(expected, reformatIndentation(source))
    }

    @Test
    fun indentsLabelBody() {
        val source = "main:\nsay 'hello'\nreturn"
        val expected = "main:\n    say 'hello'\n    return"
        assertEquals(expected, reformatIndentation(source))
    }

    @Test
    fun collapsesMultipleConsecutiveBlankLines() {
        val source = "do\n\n\n\nsay 'x'\nend"
        val expected = "do\n\n    say 'x'\nend"
        assertEquals(expected, reformatIndentation(source))
    }

    // -----------------------------------------------------------------------
    // reformatDocument: combines first-line comment + indentation
    // -----------------------------------------------------------------------

    @Test
    fun reformatDocumentAddsCommentAndIndents() {
        val source = "do\nsay 'x'\nend"
        val result = reformatDocument(source)
        assertTrue(result.startsWith(REXX_FIRST_LINE_COMMENT))
        assertTrue(result.contains("    say 'x'"))
    }

    @Test
    fun reformatDocumentDoesNotDuplicateExistingComment() {
        val source = "/* My exec */\ndo\nsay 'x'\nend"
        val result = reformatDocument(source)
        val commentCount = result.lines().count { it.trimStart().startsWith("/*") }
        assertTrue(commentCount >= 1)
        assertFalse(result.startsWith(REXX_FIRST_LINE_COMMENT))
    }

    @Test
    fun normalizesTabsTrailingSpacesAndCommentSpaces() {
        val source = "do\t\n\tsay 'x'   \n/*  a   b  */\nend\t"
        val expected = "do\n    say 'x'\n    /* a b */\nend"
        assertEquals(expected, reformatIndentation(source))
    }

    @Test
    fun reformatDocumentMergesAdjacentSingleLineCommentsSeparatedByBlankLine() {
        val source = "/* one */\n\n/* two */\nsay 'x'"
        val result = reformatDocument(source)
        assertTrue(result.contains("/* one */\n/* two */\nsay 'x'"))
    }

    @Test
    fun reformatDocumentKeepsSingleBlankWhenTwoOrMoreBetweenComments() {
        val source = "/* one */\n\n\n\n/* two */\nsay 'x'"
        val result = reformatDocument(source)
        assertTrue(result.contains("/* one */\n\n/* two */\nsay 'x'"))
        assertFalse(result.contains("/* one */\n\n\n/* two */"))
    }

    @Test
    fun reformatDocumentEndsWithSingleTrailingNewline() {
        val source = "/* one */\nsay 'x'\n \n\t\n\n"
        val result = reformatDocument(source)
        assertTrue(result.endsWith("\n"))
        assertFalse(result.endsWith("\n\n"))
    }

    @Test
    fun reformatDocumentNormalizesCrLfTrailingBlankLinesAtEnd() {
        val source = "/* one */\r\nsay 'x'\r\n \r\n\t\r\n\r\n"
        val result = reformatDocument(source)
        assertTrue(result.endsWith("\n"))
        assertFalse(result.endsWith("\n\n"))
    }
}
