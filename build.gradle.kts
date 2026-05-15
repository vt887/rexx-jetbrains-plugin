import org.jetbrains.intellij.IntelliJPluginExtension
import org.jetbrains.intellij.tasks.PatchPluginXmlTask

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.25"
    id("org.jetbrains.intellij") version "1.17.4" apply false
}

group = "org.rexxlang.intellij"
version = "0.1.0"

kotlin {
    jvmToolchain(17)
}

repositories {
    mavenCentral()
}

val isWrapperBootstrap = gradle.startParameter.taskNames.any { it == "wrapper" || it.endsWith(":wrapper") }

if (!isWrapperBootstrap) {
    apply(plugin = "org.jetbrains.intellij")

    extensions.configure<IntelliJPluginExtension> {
        version.set("2024.1")
        type.set("IC")
        updateSinceUntilBuild.set(false)
    }

    tasks.withType<PatchPluginXmlTask>().configureEach {
        sinceBuild.set("241")
        changeNotes.set(
            """
            <ul>
                <li>Initial Rexx file type registration, lexer, and syntax highlighting.</li>
            </ul>
            """.trimIndent(),
        )
    }
}

dependencies {
    testImplementation(kotlin("test-junit"))
}

tasks {
    test {
        useJUnit()
    }
}
