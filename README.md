# Rexx Language Support for IntelliJ Platform

Rexx language plugin for IntelliJ IDEA, PyCharm, and other IntelliJ Platform IDEs.

## Current Features

### Language Support
- Registers `.rexx`, `.rex`, and `.rx` files as Rexx.
- Provides a Rexx file icon.
- Highlights comments, strings, numbers, operators, punctuation, identifiers, and core keywords.
- Provides a Rexx color settings page in IDE editor color settings.

### Keyword Completion
- Autocompletes 28 core Rexx keywords (`DO`, `IF`, `SELECT`, `SAY`, `PARSE`, etc.).
- Completion is suppressed inside comments and strings.
- Triggered via `Ctrl+Space` in any `.rexx` / `.rex` / `.rx` file.

### Run Configuration
- Dedicated **Rexx** run configuration type under Run → Edit Configurations.
- Configurable fields: interpreter path, script file, working directory, program arguments.
- Supported interpreters: `rexx`, `regina`, `ooRexx` (any executable on PATH).
- Validates interpreter path is non-blank and script file exists as a regular readable file before launch.
- Executes `<interpreter> <script> [args]` via a secure local process.

### Code Formatter
- Activates **Reformat Code** (`Ctrl+Alt+L`) for Rexx files.
- Enforces the REXX first-line comment rule: if the file does not start with a block comment,
  inserts `/* The first line of a REXX exec must always be a comment. */` automatically.
- Conservative formatting — preserves string contents and comment text exactly; only adjusts indentation and blank lines.

## Limitations

- Minimal PSI scaffolding registered (`RexxParserDefinition`, `RexxFile`, `RexxPsiElement`); no semantic analysis yet.
- No semantic analysis, navigation, or refactoring.
- Structural indentation formatter requires Grammar-Kit/PSI migration (Phase 3).
- Full Rexx dialect variation support is deferred.

## Development

This project uses Gradle Kotlin DSL and the JetBrains Gradle IntelliJ Plugin.

```bash
./gradlew test
./gradlew buildPlugin
./gradlew runIde
```

The plugin depends on `com.intellij.modules.lang` in `plugin.xml`, which keeps the language support product-neutral for IntelliJ IDEA, PyCharm, and other IntelliJ Platform IDEs with language support.

## Local Installation

1. Build the plugin with `./gradlew buildPlugin`.
2. Open the target IDE.
3. Use `Settings | Plugins | Install Plugin from Disk`.
4. Select the ZIP from `build/distributions`.

## CI/CD

Every push and PR runs the full pipeline automatically via GitHub Actions.

### How CI works

| Stage | What it does |
|---|---|
| Build & Test | Compiles the plugin and runs all JUnit tests |
| Verify | Validates `plugin.xml` and plugin archive structure |
| Package | Produces an installable ZIP and uploads it as a GitHub Actions artifact |

Test reports are uploaded automatically on failure.

### How to trigger a release

```bash
git tag v0.1.0
git push origin v0.1.0
```

The release workflow builds the plugin, creates a GitHub Release, and attaches the ZIP.

### Where artifacts are stored

- **CI artifacts**: GitHub Actions → workflow run → Artifacts (30-day retention)
- **Release artifacts**: GitHub Releases page → permanent download

See [`docs/sdlc/ci-cd-architecture.md`](docs/sdlc/ci-cd-architecture.md) for full pipeline details.

---

## Architecture

- `RexxLanguage` declares the language identity.
- `RexxFileType` registers Rexx extensions and icon metadata.
- `RexxLexer` performs handwritten tokenization.
- `RexxSyntaxHighlighter` maps lexer tokens to editor colors.
- `RexxCompletionContributor` provides keyword completion.
- `RexxRunConfigurationType` / `RexxRunConfiguration` / `RexxCommandLineState` — run configuration stack.
- `format/RexxFormattingModelBuilder` + `RexxPreFormatProcessor` — formatter integration.
- `docs/language-notes.md` records the tokenization scope and parser roadmap.
