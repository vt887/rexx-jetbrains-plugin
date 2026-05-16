package org.lang.rexx

import kotlin.test.Test
import kotlin.test.assertEquals

class RexxCommentTagAnnotatorTest {
    @Test
    fun findsTodoFixmeAndNoteInCommentText() {
        val ranges = findCommentTagRanges("/* todo something FIXME and Note */")
        val matches = ranges.map { "/* todo something FIXME and Note */".substring(it) }
        assertEquals(listOf("todo", "FIXME", "Note"), matches)
    }

    @Test
    fun ignoresWordsInsideLongerIdentifiers() {
        val ranges = findCommentTagRanges("/* methodology fixed_note notebook */")
        assertEquals(emptyList(), ranges)
    }

    @Test
    fun findsBlockCommentDelimiters() {
        val text = "/* TODO */"
        val ranges = findCommentDelimiterRanges(text)
        val matches = ranges.map { text.substring(it) }
        assertEquals(listOf("/*", "*/"), matches)
    }
}
