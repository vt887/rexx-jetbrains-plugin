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
                <li>Initial Rexx file type registration, lexer, and syntax highlighting.</li>
            </ul>
        """.trimIndent()
    }
}

tasks {
    test {
        useJUnit()
    }
}
