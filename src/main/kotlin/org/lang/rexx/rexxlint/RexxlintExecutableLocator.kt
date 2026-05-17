package org.lang.rexx.rexxlint

import com.intellij.openapi.util.SystemInfoRt
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

open class RexxlintExecutableLocator(
    private val envPath: String? = System.getenv("PATH"),
) {
    open fun findExecutable(configuredPath: String): Path? {
        candidateFromConfiguredPath(configuredPath)?.let { return it }
        candidateFromPathEnv()?.let { return it }
        return commonInstallLocations().firstOrNull(::isExecutable)
    }

    private fun candidateFromConfiguredPath(configuredPath: String): Path? {
        if (configuredPath.isBlank()) return null
        val path = Paths.get(configuredPath)
        return path.takeIf(::isExecutable)
    }

    private fun candidateFromPathEnv(): Path? =
        envPath
            ?.split(File.pathSeparator)
            ?.asSequence()
            ?.map(String::trim)
            ?.filter(String::isNotBlank)
            ?.map { Paths.get(it).resolve(executableName()) }
            ?.firstOrNull(::isExecutable)

    private fun commonInstallLocations(): Sequence<Path> =
        when {
            SystemInfoRt.isWindows -> {
                sequenceOf(
                    Paths.get("C:/Program Files/rexxlint/rexxlint.exe"),
                    Paths.get("C:/Program Files (x86)/rexxlint/rexxlint.exe"),
                    Paths.get(System.getenv("LOCALAPPDATA") ?: "", "Programs", "rexxlint", "rexxlint.exe"),
                )
            }

            SystemInfoRt.isMac -> {
                sequenceOf(
                    Paths.get("/opt/homebrew/bin/rexxlint"),
                    Paths.get("/usr/local/bin/rexxlint"),
                    Paths.get("/usr/bin/rexxlint"),
                )
            }

            else -> {
                sequenceOf(
                    Paths.get("/usr/local/bin/rexxlint"),
                    Paths.get("/usr/bin/rexxlint"),
                    Paths.get("/snap/bin/rexxlint"),
                )
            }
        }

    private fun executableName(): String = if (SystemInfoRt.isWindows) "rexxlint.exe" else "rexxlint"

    private fun isExecutable(path: Path): Boolean = Files.isRegularFile(path) && Files.isExecutable(path)
}
