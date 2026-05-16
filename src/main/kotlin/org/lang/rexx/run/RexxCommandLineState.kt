package org.lang.rexx.run

import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.configurations.ParametersList
import com.intellij.execution.configurations.RuntimeConfigurationError
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import java.nio.file.Path

internal fun buildRexxCommandLine(
    interpreterPath: String,
    scriptPath: String,
    workingDirectory: String,
    programArguments: String,
): GeneralCommandLine {
    val commandLine = GeneralCommandLine().withExePath(interpreterPath)
    commandLine.addParameter(scriptPath)
    ParametersList.parse(programArguments).forEach(commandLine::addParameter)

    if (workingDirectory.isNotBlank()) {
        commandLine.withWorkDirectory(workingDirectory)
    }

    return commandLine
}

class RexxCommandLineState(
    environment: ExecutionEnvironment,
    private val configuration: RexxRunConfiguration,
) : CommandLineState(environment) {
    private fun resolveScriptPath(): String =
        if (configuration.useCurrentFile) {
            resolveCurrentRexxFilePath(configuration.project)
                ?: throw RuntimeConfigurationError("No active Rexx file in editor.")
        } else {
            configuration.scriptPath
        }

    private fun resolveWorkingDirectory(scriptPath: String): String {
        if (configuration.workingDirectory.isNotBlank()) {
            return configuration.workingDirectory
        }
        return try {
            Path
                .of(scriptPath)
                .parent
                ?.toString()
                .orEmpty()
        } catch (_: Exception) {
            ""
        }
    }

    @Throws(ExecutionException::class)
    override fun startProcess(): ProcessHandler {
        val scriptPath = resolveScriptPath()
        return OSProcessHandler(
            buildRexxCommandLine(
                interpreterPath = configuration.interpreterPath,
                scriptPath = scriptPath,
                workingDirectory = resolveWorkingDirectory(scriptPath),
                programArguments = configuration.programArguments,
            ),
        )
    }
}
