package org.lang.rexx

import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.impl.DocumentImpl
import com.intellij.openapi.util.TextRange
import kotlin.test.Test
import kotlin.test.assertEquals
import org.lang.rexx.rexxlint.RexxlintDiagnostic
import org.lang.rexx.rexxlint.toTextRange

class RexxFormattingTest {
    @Test
    fun mapsDiagnosticCoordinatesIntoEditorRange() {
        val document = DocumentImpl("say 'hi'\nreturn\n")
        val diagnostic = RexxlintDiagnostic(
            severity = HighlightSeverity.ERROR,
            message = "Unexpected token",
            line = 1,
            column = 5,
            endLine = 1,
            endColumn = 9,
        )

        assertEquals(TextRange(4, 8), diagnostic.toTextRange(document))
    }
}
