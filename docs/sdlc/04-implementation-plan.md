# Implementation Plan

## Current Baseline (as of Phase 3 completion)

MVP implementation is **complete**. All Phase 1–3 deliverables are present and
compilable. The repository is ready for Phase 4 validation.

### Implemented

| Component | File | Status |
|---|---|---|
| Language singleton | `RexxLanguage.kt` | ✅ Done |
| File type + extensions | `RexxFileType.kt` | ✅ Done |
| Icons | `RexxIcons.kt`, `icons/rexx.svg` | ✅ Done |
| Hand-written lexer | `RexxLexer.kt` | ✅ Done |
| Lexer adapter | `RexxLexerAdapter.kt` | ✅ Done |
| Token types | `RexxTokenTypes.kt` | ✅ Done |
| Token sets | `RexxTokenSets.kt` | ✅ Done |
| Syntax highlighter | `RexxSyntaxHighlighter.kt` | ✅ Done |
| Highlighter factory | `RexxSyntaxHighlighterFactory.kt` | ✅ Done |
| Text attributes | `RexxTextAttributes.kt` | ✅ Done |
| Color settings page | `RexxColorSettingsPage.kt` | ✅ Done |
| Plugin descriptor | `META-INF/plugin.xml` | ✅ Done |
| Lexer tests (5) | `RexxLexerTest.kt` | ✅ Done |
| Highlighter tests (7) | `RexxSyntaxHighlighterTest.kt` | ✅ Done |
| Sample files | `samples/*.rexx`, `*.rex`, `*.rx` | ✅ Done |
| Gradle wrapper | `gradlew`, `gradle/wrapper/` | ✅ Done |
| SDLC docs 00–10 | `docs/sdlc/` | ✅ Done |

---

## Phase 4 — Validation (Next)

**Goal:** confirm the build compiles, all tests pass, and the plugin loads in a
sandboxed IDE instance.

### Tasks

1. Run `./gradlew clean build` — fix any compilation errors.
2. Run `./gradlew test` — confirm all 12 tests pass.
3. Run `./gradlew runIde` — visually verify:
   - `.rexx` / `.rex` / `.rx` files are recognized.
   - Keywords, strings, comments, numbers are highlighted correctly.
   - Color settings page appears under **Settings → Editor → Color Scheme → Rexx**.
4. Document results in `docs/sdlc/05-test-strategy.md`.

### Acceptance Criteria

- Zero compilation errors.
- Zero test failures.
- Plugin starts in sandbox IDE without warnings or errors in the IDE log.

---

## Phase 5 — Hardening

**Goal:** clean up rough edges before release preparation.

### Tasks

1. **Deprecated API audit** — run `./gradlew buildPlugin verifyPlugin`, fix any
   deprecated API usages flagged by Plugin Verifier.
2. **`plugin.xml` review** — confirm vendor email, description, and
   `changeNotes` are production-ready.
3. **Test coverage gaps** — add edge-case lexer tests:
   - Unterminated strings spanning to EOF.
   - Nested comments (depth > 1).
   - Mixed-case keyword variants.
   - Stem variable notation (`stem.1.sub`).
4. **Package split (optional)** — move lexer/highlighting classes into
   sub-packages before any Phase 2 (PSI) work begins.
5. Security and compatibility reviews — see `docs/sdlc/06-security-review.md`
   and `docs/sdlc/07-compatibility-review.md`.

---

## Phase 6 — Release Preparation

**Goal:** prepare for JetBrains Marketplace submission.

### Tasks

1. Finalize `README.md` with screenshots and usage instructions.
2. Update `CHANGELOG.md` with `0.1.0` entry.
3. Prepare Marketplace listing metadata (icon, description, category).
4. Run `./gradlew buildPlugin` and validate the output `.zip`.
5. Submit to JetBrains Marketplace or publish to GitHub Releases.

---

## Validation Commands

```bash
./gradlew clean build          # compile check
./gradlew test                 # unit tests
./gradlew runIde               # manual IDE smoke test
./gradlew buildPlugin          # produce distributable zip
./gradlew verifyPlugin         # structural validation
./gradlew runPluginVerifier    # compatibility check against target IDEs
```

---

## Dependency Order (future features)

When Phase 2 (PSI/parser) work begins:

1. Package split (`lexer`, `highlighting` sub-packages).
2. `ParserDefinition` + `IFileElementType`.
3. Minimal AST node set.
4. PSI wrappers for identifiers/labels.
5. Structure-aware features (folding, breadcrumbs, navigation).
