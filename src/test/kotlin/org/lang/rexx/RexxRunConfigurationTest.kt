package org.lang.rexx

import com.intellij.execution.configurations.RuntimeConfigurationError
import org.lang.rexx.run.buildRexxCommandLine
import org.lang.rexx.run.validateRexxRunConfiguration
import java.nio.file.Path
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class RexxRunConfigurationTest {
    @Test
    fun buildsCommandLineWithInterpreterScriptAndArguments() {
        val commandLine = buildRexxCommandLine(
            interpreterPath = "rexx",
            scriptPath = "example.rexx",
            workingDirectory = "samples",
            programArguments = "--flag \"two words\"",
        )

        assertEquals("rexx", commandLine.exePath)
        assertEquals(listOf("example.rexx", "--flag", "two words"), commandLine.parametersList.parameters)
        assertTrue(commandLine.workDirectory.toString().endsWith("samples"))
    }

    @Test
    fun requiresExistingScriptPath() {
        val error = assertFailsWith<RuntimeConfigurationError> {
            validateRexxRunConfiguration("rexx", "missing-script.rexx")
        }

        assertTrue(error.message.orEmpty().contains("does not exist"))
    }

    @Test
    fun acceptsExistingScriptPath() {
        validateRexxRunConfiguration("rexx", Path.of("example.rexx").toAbsolutePath().toString())
    }
}
