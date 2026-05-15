# IntelliJ Rexx Plugin — Architecture

## Scope and Goals

MVP goal: stable Rexx language bootstrap for IntelliJ Platform IDEs with:
- file type association
- lexer tokenization
- syntax highlighting
- color settings page

Primary targets:
- IntelliJ IDEA
- PyCharm

Tech baseline:
- Kotlin
- Gradle Kotlin DSL
- IntelliJ Platform Gradle Plugin `1.17.4`
- IntelliJ Platform SDK `2023.3.8` (`IC`), `sinceBuild=233`

Out of MVP scope:
- parser/PSI tree
- code completion, inspections, formatter, refactorings
- execution/debug integration

---

## Actual Package Structure (MVP)

All MVP classes currently live in a single package. This is intentional for MVP
simplicity. The refactoring path below applies when adding parser/PSI features.

```text
org.rexxlang.intellij.rexx          ← current MVP package (all classes here)
  RexxLanguage.kt                   language singleton
  RexxFileType.kt                   file type + extension registration
  RexxIcons.kt                      icon loading from resources/icons/rexx.svg
  RexxLexer.kt                      hand-written LexerBase implementation
  RexxLexerAdapter.kt               MergingLexerAdapter wrapper for editor use
  RexxTokenTypes.kt                 token type constants + RexxTokenType helper
  RexxTokenSets.kt                  shared TokenSet groupings
  RexxSyntaxHighlighter.kt          token → TextAttributesKey mapping
  RexxSyntaxHighlighterFactory.kt   SyntaxHighlighterFactory registration
  RexxTextAttributes.kt             TextAttributesKey declarations
  RexxColorSettingsPage.kt          Color & Fonts settings page
```

### Proposed Future Package Split (pre-PSI)

Before introducing parser work, split into feature packages:

```text
org.rexxlang.intellij.rexx
  RexxLanguage.kt
  RexxFileType.kt
  RexxIcons.kt

org.rexxlang.intellij.rexx.lexer
  RexxLexer.kt
  RexxLexerAdapter.kt
  RexxTokenTypes.kt
  RexxTokenSets.kt

org.rexxlang.intellij.rexx.highlighting
  RexxSyntaxHighlighter.kt
  RexxSyntaxHighlighterFactory.kt
  RexxTextAttributes.kt
  RexxColorSettingsPage.kt

org.rexxlang.intellij.rexx.psi        (Phase 2 — future)
org.rexxlang.intellij.rexx.parser     (Phase 2 — future)
```

---

## Extension Points and Registration Flow

Registered extension points in `plugin.xml`:

| Extension point | Implementation class |
|---|---|
| `fileType` | `RexxFileType` |
| `lang.syntaxHighlighterFactory` | `RexxSyntaxHighlighterFactory` |
| `colorSettingsPage` | `RexxColorSettingsPage` |

Plugin dependency: `com.intellij.modules.lang` only.  
Do not add `com.intellij.modules.idea` or other IDE-specific modules until required.

**Registration/load flow:**

1. IDE loads plugin descriptor (`plugin.xml`).
2. `fileType` maps `.rexx`, `.rex`, `.rx` → `RexxFileType` → binds `RexxLanguage`.
3. Editor requests `RexxSyntaxHighlighterFactory` for the language.
4. Factory returns `RexxSyntaxHighlighter`.
5. Highlighter constructs a `RexxLexerAdapter`, scans tokens, maps each to a
   `TextAttributesKey` defined in `RexxTextAttributes`.
6. Color settings page (`RexxColorSettingsPage`) exposes those keys in
   **Settings → Editor → Color Scheme → Rexx**.

---

## Lexer Design

### Strategy

Hand-written `LexerBase` (`RexxLexer`) — appropriate for MVP because:
- predictable, testable behavior without grammar toolchain
- full control over nested comment handling and string escape rules
- no JFlex/Grammar-Kit dependency until grammar complexity justifies it

### Token Model

| Token type | Description |
|---|---|
| `COMMENT` | Block `/* ... */` with nesting, line-end `--` |
| `KEYWORD` | Case-insensitive Rexx keywords (`DO`, `END`, `IF`, `SAY`, etc.) |
| `IDENTIFIER` | Symbols, stem variables (`stem.1`) |
| `STRING` | Single- or double-quoted, with escaped quotes |
| `NUMBER` | Integer, decimal, scientific notation |
| `OPERATOR` | Arithmetic, comparison, logical, string operators |
| `PUNCTUATION` | `.`, `;`, `(`, `)`, `,` |
| `WHITE_SPACE` | Merged via `RexxLexerAdapter` |
| `BAD_CHARACTER` | Unrecognized input |

### Invariants

- Single-pass O(n), no backtracking.
- Multi-char operators scanned longest-first (deterministic precedence).
- Nested comments tracked via depth counter.
- Unterminated strings/comments emit a token to end-of-file (no crash).

---

## Future Parser and PSI Roadmap

When MVP is validated and lexer contracts are stable:

1. Add `ParserDefinition` + `IFileElementType` + `PsiFile`.
2. Add minimal AST nodes for top-level statements.
3. Add PSI wrappers for identifiers and labels.
4. Enable structure-aware features incrementally (folding, breadcrumbs).
5. Consider Grammar-Kit only when grammar complexity justifies generated parser cost.

**Rule:** parser packages must not be imported from highlighting code. Highlighting
must remain lexer-only and work independently of PSI availability.

---

## Testing Approach

| Layer | Tool | Coverage target |
|---|---|---|
| Lexer token streams | `RexxLexerTest` (JUnit) | All token types, edge cases |
| Highlighter mapping | `RexxSyntaxHighlighterTest` (JUnit) | All attribute keys |
| Plugin compatibility | `verifyPlugin` + Plugin Verifier | Before each release |

**Recommended additions (Phase 4/5):**
- Fixture-driven lexer corpus under `src/test/resources/rexx/`
- Edge-case suite: unterminated strings, nested comments, numeric formats, mixed-case keywords

**Minimum release gates:**
```bash
./gradlew test
./gradlew buildPlugin verifyPlugin
./gradlew runPluginVerifier
```

---

## Maintainability Risks

| Risk | Mitigation |
|---|---|
| Lexer regressions from ad-hoc keyword changes | Fixture corpus + non-regression tests |
| Monolithic package growth before PSI | Enforce package split before parser work |
| IntelliJ API compatibility breakage | Plugin Verifier on every release |
| Highlighting coupled to future PSI internals | Keep highlighting lexer-only, no PSI imports |
| Rexx dialect drift | Explicit supported-dialect note + keyword versioning |

---

## Architecture Decisions

- **No services, components, or background tasks** in MVP.
- **Lexer-first**: harden tokenization before introducing grammar.
- **Single dependency**: `com.intellij.modules.lang` only.
- **No deprecated APIs**: enforced by Plugin Verifier.
