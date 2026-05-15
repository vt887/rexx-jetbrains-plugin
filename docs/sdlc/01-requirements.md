# Requirements

## Functional Requirements

- Register Rexx language and file type.
- Support file extensions: `.rexx`, `.rex`, `.rx`.
- Provide syntax highlighting for:
  - comments
  - strings
  - numbers
  - operators
  - keywords
  - punctuation/identifiers
- Provide color settings page integration.
- Include sample Rexx source files.
- Include automated tests for implemented MVP behavior.

## Non-Functional Requirements

- Kotlin implementation with idiomatic, maintainable code.
- Gradle Kotlin DSL build configuration.
- IntelliJ Platform Gradle Plugin integration.
- Modular structure separating language, file type, lexer, highlighting, and tests.
- Avoid deprecated IntelliJ APIs.
- No unnecessary permissions or unsafe runtime behavior.

## Platform Requirements

- IntelliJ IDEA support.
- PyCharm support.
- Base dependency should remain language-platform level (`com.intellij.modules.lang`).

## MVP Scope (Phase 1)

- Build scaffold and plugin metadata.
- Language/file registration.
- Lexer/token model and syntax highlighter.
- Color settings page.
- Basic tests.
- Basic documentation.

## Out Of Scope (MVP)

- Full parser/PSI implementation.
- Advanced inspections/refactorings.
- Formatter and run configuration.
- Marketplace publishing automation.
