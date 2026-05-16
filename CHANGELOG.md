# Changelog

## Unreleased (Phase 2)

- Added keyword completion for 28 core Rexx keywords (`Ctrl+Space`).
- Added Rexx Run Configuration (interpreter, script, working dir, args).
- Added code formatter integration with first-line comment enforcement.
- Added unit tests for completion, run configuration, and formatter.
- Upgraded build stack: IntelliJ Platform Gradle Plugin 2.16.0, Kotlin 2.1.21, JBR 21, IC 2025.2.
- Upgraded Gradle wrapper to 9.5.1.
- Renamed plugin ID from `org.rexxlang.intellij` to `org.rexxlang.rexx`.
- CI: upgraded GitHub Actions to Node 24 native versions.
- CI: enabled `gradle-version: current` and `cache-provider: basic`.

## 0.1.0

- Added minimal IntelliJ Platform plugin scaffold.
- Added Rexx file type registration for common Rexx extensions.
- Added handwritten lexer and syntax highlighter.
- Added lexer unit tests and language notes.
