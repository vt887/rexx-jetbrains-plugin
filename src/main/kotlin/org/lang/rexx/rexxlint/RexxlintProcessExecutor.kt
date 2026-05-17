package org.lang.rexx.rexxlint

import com.intellij.execution.configurations.GeneralCommandLine
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

data class ProcessExecutionResult(
    val exitCode: Int? = null,
    val stdout: String = "",
    val stderr: String = "",
    val timedOut: Boolean = false,
    val exceptionMessage: String? = null,
)

fun interface RexxlintProcessExecutor {
    fun execute(
        commandLine: GeneralCommandLine,
        stdin: String,
        timeout: Duration,
    ): ProcessExecutionResult
}

class DefaultRexxlintProcessExecutor : RexxlintProcessExecutor {
    override fun execute(
        commandLine: GeneralCommandLine,
        stdin: String,
        timeout: Duration,
    ): ProcessExecutionResult {
        return try {
            val process = commandLine.createProcess()
            process.outputStream.bufferedWriter(StandardCharsets.UTF_8).use { writer ->
                writer.write(stdin)
            }

            val pool = Executors.newFixedThreadPool(2)
            try {
                val stdout = pool.submit<String> { process.inputStream.bufferedReader(StandardCharsets.UTF_8).use { it.readText() } }
                val stderr = pool.submit<String> { process.errorStream.bufferedReader(StandardCharsets.UTF_8).use { it.readText() } }
                val finished = process.waitFor(timeout.toMillis(), TimeUnit.MILLISECONDS)
                if (!finished) {
                    process.destroyForcibly()
                    return ProcessExecutionResult(timedOut = true)
                }
                ProcessExecutionResult(
                    exitCode = process.exitValue(),
                    stdout = stdout.get(),
                    stderr = stderr.get(),
                )
            } finally {
                pool.shutdownNow()
            }
        } catch (error: Exception) {
            ProcessExecutionResult(exceptionMessage = error.message ?: error::class.java.simpleName)
        }
    }
}
