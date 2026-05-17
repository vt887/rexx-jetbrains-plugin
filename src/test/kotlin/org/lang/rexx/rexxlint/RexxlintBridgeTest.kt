package org.lang.rexx.rexxlint

import com.intellij.execution.configurations.GeneralCommandLine
import java.nio.file.Path
import java.time.Duration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class RexxlintBridgeTest {
    @Test
    fun reportsMissingExecutable() {
        val bridge = RexxlintBridge(
            settingsProvider = { "" },
            executableLocator = object : RexxlintExecutableLocator(null) {
                override fun findExecutable(configuredPath: String): Path? = null
            },
        )

        val result = assertIs<RexxlintCommandResult.Failure>(bridge.lint("say 'hi'"))
        assertEquals(RexxlintFailureCode.EXECUTABLE_NOT_FOUND, result.code)
    }

    @Test
    fun formatterBridgeUsesGeneralCommandLineAndStdIn() {
        var capturedCommandLine: GeneralCommandLine? = null
        var capturedStdin: String? = null
        val bridge = RexxlintBridge(
            settingsProvider = { "/custom/rexxlint" },
            executableLocator = object : RexxlintExecutableLocator(null) {
                override fun findExecutable(configuredPath: String): Path = Path.of(configuredPath)
            },
            processExecutor = RexxlintProcessExecutor { commandLine, stdin, _ ->
                capturedCommandLine = commandLine
                capturedStdin = stdin
                ProcessExecutionResult(exitCode = 0, stdout = "formatted\n")
            },
        )

        val result = assertIs<RexxlintCommandResult.Success<String>>(bridge.format("say 'hi'", "/tmp/demo.rexx"))
        assertEquals("formatted\n", result.value)
        assertEquals("/custom/rexxlint", capturedCommandLine?.exePath)
        assertEquals(listOf("format", "--stdin", "--path", "/tmp/demo.rexx"), capturedCommandLine?.parametersList?.list)
        assertEquals("say 'hi'", capturedStdin)
    }

    @Test
    fun reportsTimeoutsGracefully() {
        val bridge = RexxlintBridge(
            settingsProvider = { "/custom/rexxlint" },
            executableLocator = object : RexxlintExecutableLocator(null) {
                override fun findExecutable(configuredPath: String): Path = Path.of(configuredPath)
            },
            processExecutor = RexxlintProcessExecutor { _, _, _ -> ProcessExecutionResult(timedOut = true) },
            timeout = Duration.ofSeconds(1),
        )

        val result = assertIs<RexxlintCommandResult.Failure>(bridge.format("say 'hi'"))
        assertEquals(RexxlintFailureCode.TIMEOUT, result.code)
        assertTrue(result.message.contains("1s"))
    }
}
