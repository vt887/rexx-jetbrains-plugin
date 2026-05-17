import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    id("org.jetbrains.kotlin.jvm") version "2.3.21"
    id("org.jetbrains.intellij.platform") version "2.16.0"
}

group = "org.lang.rexx"
version = "0.1.0"

kotlin {
    jvmToolchain(21)
}

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.20.0")
    intellijPlatform {
        create("IC", "2025.2")
        testFramework(TestFrameworkType.Platform)
    }
    testImplementation(kotlin("test-junit"))
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild = "252"
        }
        changeNotes = """
            <ul>
                <li>Added Rexx keyword completion for core control-flow statements.</li>
                <li>Added Rexx run configuration support for interpreter-based execution.</li>
                <li>Delegated Rexx linting and formatting to rexxlint via an external process bridge.</li>
            </ul>
        """.trimIndent()
    }

    pluginVerification {
        ides {
            current()
        }
    }
}

tasks {
    test {
        useJUnit()
    }

    runIde {
        // Suppress OpenTelemetry bootstrap failures in local sandbox runs.
        jvmArgs("-Dotel.sdk.disabled=true")
        environment("OTEL_SDK_DISABLED", "true")
    }
}
