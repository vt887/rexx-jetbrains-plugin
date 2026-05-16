package org.lang.rexx.run

import org.junit.Assume.assumeTrue
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RexxRunConfigurationTest {
    @Test
    fun detectsInstalledInterpretersFromPath() {
        assumeTrue(
            "Test requires POSIX filesystem (skipped on Windows)",
            !System.getProperty("os.name", "").lowercase().contains("windows"),
        )
        val dir = Files.createTempDirectory("rexx-test-path")
        val rexx = dir.resolve("rexx")
        Files.writeString(rexx, "#!/bin/sh\n")
        rexx.toFile().setExecutable(true)

        val detected = detectSystemRexxInterpreters(dir.toString())
        assertTrue(detected.contains(rexx.toString()))
    }

    @Test
    fun fallsBackToDefaultWhenNothingDetected() {
        val dir = Files.createTempDirectory("rexx-test-empty-path")
        val detected = detectSystemRexxInterpreters(dir.toString())
        assertEquals(listOf(DEFAULT_REXX_INTERPRETER), detected)
    }
}
