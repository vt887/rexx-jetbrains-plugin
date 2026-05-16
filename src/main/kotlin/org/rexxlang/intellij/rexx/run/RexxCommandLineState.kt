package org.rexxlang.intellij.rexx.run

import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.configurations.ParametersList
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment

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
    @Throws(ExecutionException::class)
    override fun startProcess(): ProcessHandler = OSProcessHandler(
        buildRexxCommandLine(
            interpreterPath = configuration.interpreterPath,
            scriptPath = configuration.scriptPath,
            workingDirectory = configuration.workingDirectory,
            programArguments = configuration.programArguments,
        ),
    )
}
