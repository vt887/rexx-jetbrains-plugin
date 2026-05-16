# Kotlin linter architecture

## Tool
- **Linter / formatter:** `ktlint`
- **Pinned version in automation:** `1.8.0`

## Scope
Ktlint runs on repository Kotlin sources under:

```text
src/**/*.kt
```

This covers both production and test Kotlin code while excluding generated output and build artifacts.

## Make integration
The repository exposes ktlint through the top-level `Makefile`:

- `make lint` — runs `ktlint "src/**/*.kt"`
- `make format` — runs `ktlint --format "src/**/*.kt"`
- `make versions` — prints `ktlint --version` together with Java, Kotlin, Gradle, Rexx, and Git versions

`make format` is the supported auto-fix entrypoint; the earlier `lint-fix` name was intentionally retired.

## CI integration
Ktlint is installed and executed before compilation in both GitHub Actions workflows.

### `.github/workflows/ci.yml`
- `Install ktlint`
- `Lint`
- `Build`
- `Test`

Lint runs only in the **Build & Test** job so style failures stop the pipeline early.

### `.github/workflows/release.yml`
- `Install ktlint`
- `Lint`
- `Build`
- `Verify plugin`
- `Build plugin ZIP`

Release artifacts are therefore built only from lint-clean Kotlin sources.

## Rules enforced
The integration uses the standard ktlint ruleset. Violations fixed during adoption included:
- spacing between annotated declarations
- multiline expression wrapping
- function signature wrapping
- import ordering
- empty first line inside class body

No custom rule set or `.editorconfig` override was introduced as part of the recorded integration work.

## Suppressing a rule
Use ktlint suppression comments sparingly and only when the default rule materially hurts readability.

Block suppression example:

```kotlin
/* ktlint-disable standard:function-signature */
fun veryLongFunctionName(
    firstArgument: String,
    secondArgument: String,
) = Unit
/* ktlint-enable standard:function-signature */
```

Line suppression example:

```kotlin
// ktlint-disable-next-line standard:no-wildcard-imports
import foo.*
```

If a suppression needs to apply to the full file, prefer Kotlin's file-level suppression form:

```kotlin
@file:Suppress("ktlint:standard:function-signature")
```

## Operational guidance
- Run `make format` before committing broad Kotlin edits.
- Run `make lint` locally before pushing if Kotlin files changed.
- Keep CI and local commands aligned by updating the `Makefile` first, then consuming it from workflows.
