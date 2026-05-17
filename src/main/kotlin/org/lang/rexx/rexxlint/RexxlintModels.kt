package org.lang.rexx.rexxlint

import com.intellij.lang.annotation.HighlightSeverity

enum class RexxlintFailureCode {
    EXECUTABLE_NOT_FOUND,
    TIMEOUT,
    PROCESS_ERROR,
    INVALID_OUTPUT,
    IO_ERROR,
}

sealed interface RexxlintCommandResult<out T> {
    data class Success<T>(val value: T) : RexxlintCommandResult<T>

    data class Failure(
        val code: RexxlintFailureCode,
        val message: String,
        val details: String? = null,
    ) : RexxlintCommandResult<Nothing>
}

data class RexxlintDiagnostic(
    val severity: HighlightSeverity,
    val message: String,
    val line: Int,
    val column: Int,
    val endLine: Int? = null,
    val endColumn: Int? = null,
    val code: String? = null,
)
