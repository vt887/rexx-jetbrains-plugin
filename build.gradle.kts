plugins {
    id("org.jetbrains.kotlin.jvm") version "2.1.21"
    id("org.jetbrains.intellij.platform") version "2.16.0"
}

group = "org.rexxlang.intellij"
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
    intellijPlatform {
        create("IC", "2025.2")
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
                <li>Added conservative formatter integration with a mandatory first-line comment rule.</li>
            </ul>
        """.trimIndent()
    }
}

tasks {
    test {
        useJUnit()
    }
}
