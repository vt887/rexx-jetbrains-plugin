package org.lang.rexx.rexxlint

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.time.Duration

private val DEFAULT_REXXLINT_TIMEOUT: Duration = Duration.ofSeconds(10)

class RexxlintBridge(
    private val settingsProvider: () -> String,
    private val executableLocator: RexxlintExecutableLocator = RexxlintExecutableLocator(),
    private val processExecutor: RexxlintProcessExecutor = DefaultRexxlintProcessExecutor(),
    private val jsonParser: RexxlintJsonParser = RexxlintJsonParser(),
    private val timeout: Duration = DEFAULT_REXXLINT_TIMEOUT,
) {
    fun lint(
        source: String,
        filePath: String? = null,
    ): RexxlintCommandResult<List<RexxlintDiagnostic>> =
        when (val result = runCommand(listOf("check", "--stdin", "--output", "json"), source, filePath)) {
            is RexxlintCommandResult.Success -> jsonParser.parseDiagnostics(result.value)
            is RexxlintCommandResult.Failure -> result
        }

    fun format(
        source: String,
        filePath: String? = null,
    ): RexxlintCommandResult<String> = runCommand(listOf("format", "--stdin"), source, filePath)

    private fun runCommand(
        arguments: List<String>,
        source: String,
        filePath: String?,
    ): RexxlintCommandResult<String> {
        val executable =
            executableLocator.findExecutable(settingsProvider())
                ?: return RexxlintCommandResult.Failure(
                    code = RexxlintFailureCode.EXECUTABLE_NOT_FOUND,
                    message = "rexxlint executable was not found. Configure it in Settings | Tools | Rexx Lint or install it on PATH.",
                )

        val commandLine = buildCommandLine(executable, arguments, filePath)
        val execution = processExecutor.execute(commandLine, source, timeout)
        if (execution.timedOut) {
            return RexxlintCommandResult.Failure(
                code = RexxlintFailureCode.TIMEOUT,
                message = "rexxlint timed out after ${timeout.toSeconds()}s.",
            )
        }
        if (execution.exceptionMessage != null) {
            return RexxlintCommandResult.Failure(
                code = RexxlintFailureCode.IO_ERROR,
                message = "Failed to execute rexxlint.",
                details = execution.exceptionMessage,
            )
        }
        if (execution.exitCode != 0) {
            return RexxlintCommandResult.Failure(
                code = RexxlintFailureCode.PROCESS_ERROR,
                message = execution.stderr.ifBlank { "rexxlint exited with code ${execution.exitCode}." }.trim(),
                details = execution.stdout.takeIf(String::isNotBlank),
            )
        }
        return RexxlintCommandResult.Success(execution.stdout)
    }

    private fun buildCommandLine(
        executable: Path,
        arguments: List<String>,
        filePath: String?,
    ): GeneralCommandLine =
        GeneralCommandLine()
            .withExePath(executable.toString())
            .withCharset(StandardCharsets.UTF_8)
            .withParameters(arguments + filePathArgument(filePath))

    private fun filePathArgument(filePath: String?): List<String> =
        filePath?.takeIf(String::isNotBlank)?.let { listOf("--path", it) } ?: emptyList()
}

@Service(Service.Level.APP)
class RexxlintBridgeService {
    private val bridge: RexxlintBridge by lazy {
        RexxlintBridge(settingsProvider = { service<RexxlintSettingsState>().executablePath })
    }

    fun lint(
        source: String,
        filePath: String? = null,
    ): RexxlintCommandResult<List<RexxlintDiagnostic>> = bridge.lint(source, filePath)

    fun format(
        source: String,
        filePath: String? = null,
    ): RexxlintCommandResult<String> = bridge.format(source, filePath)
}
