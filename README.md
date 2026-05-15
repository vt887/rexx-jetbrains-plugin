# Rexx Language Support for IntelliJ Platform

Minimal Rexx language plugin for IntelliJ IDEA, PyCharm, and other IntelliJ Platform IDEs.

## Current Features

- Registers `.rexx`, `.rex`, and `.rx` files as Rexx.
- Provides a Rexx file icon.
- Highlights comments, strings, numbers, operators, punctuation, identifiers, and core keywords.
- Provides a Rexx color settings page in IDE editor color settings.
- Includes lexer unit tests for control flow, nested comments, escaped strings, and compound symbols.

## MVP Scope and Limitations

This plugin currently provides basic lexical/editor support for Rexx.

### Scope (MVP)
- Tokenization and highlighting for comments, identifiers, numbers, strings, operators, punctuation, and core keywords.
- File type registration and editor color customization integration.

### Limitations (MVP)
- No parser-based semantic analysis.
- No completion, navigation, or refactoring.
- No formatter or run configuration yet.
- Full Rexx dialect variation support is deferred to post-MVP phases.

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
- `RexxLexer` performs handwritten tokenization for the MVP.
- `RexxSyntaxHighlighter` maps lexer tokens to editor colors.
- `docs/language-notes.md` records the tokenization scope and parser roadmap.
