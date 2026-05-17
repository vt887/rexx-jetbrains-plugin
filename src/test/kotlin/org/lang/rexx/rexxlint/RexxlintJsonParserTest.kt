package org.lang.rexx.rexxlint

import com.intellij.lang.annotation.HighlightSeverity
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class RexxlintJsonParserTest {
    private val parser = RexxlintJsonParser()

    @Test
    fun parsesDiagnosticsArray() {
        val json =
            """
            {
              "diagnostics": [
                {
                  "severity": "error",
                  "message": "Unexpected token",
                  "line": 3,
                  "column": 5,
                  "endLine": 3,
                  "endColumn": 8,
                  "code": "E100"
                }
              ]
            }
            """.trimIndent()

        val result = assertIs<RexxlintCommandResult.Success<List<RexxlintDiagnostic>>>(parser.parseDiagnostics(json))
        assertEquals(1, result.value.size)
        assertEquals(
            RexxlintDiagnostic(
                severity = HighlightSeverity.ERROR,
                message = "Unexpected token",
                line = 3,
                column = 5,
                endLine = 3,
                endColumn = 8,
                code = "E100",
            ),
            result.value.single(),
        )
    }

    @Test
    fun parsesRangeBasedDiagnostics() {
        val json =
            """
            [
              {
                "severity": "warning",
                "message": "Mixed case keyword",
                "range": {
                  "start": { "line": 2, "column": 1 },
                  "end": { "line": 2, "column": 7 }
                }
              }
            ]
            """.trimIndent()

        val result = assertIs<RexxlintCommandResult.Success<List<RexxlintDiagnostic>>>(parser.parseDiagnostics(json))
        assertEquals(HighlightSeverity.WARNING, result.value.single().severity)
        assertEquals(2, result.value.single().line)
        assertEquals(7, result.value.single().endColumn)
    }
}
