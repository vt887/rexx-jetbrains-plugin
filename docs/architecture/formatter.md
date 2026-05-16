# Rexx formatter architecture

## Overview
`RexxPreFormatProcessor` is the current formatter entrypoint for Rexx files. It is intentionally conservative: the implementation is text-based, runs before IntelliJ formatting proper, and avoids PSI-dependent structure because the plugin is still largely lexer-first.

**Primary file:** `/Users/tymoshv/MyPetProjects/rexx-jetbrains-plugin/src/main/kotlin/org/lang/rexx/format/RexxPreFormatProcessor.kt`

## Processing pipeline
1. `RexxPreFormatProcessor.process()` checks that the current file language is Rexx.
2. It reads the IntelliJ code style indent width from `CodeStyle.getSettings(file)`.
3. The configured indent is normalized with `coerceAtLeast(1)`.
4. `reformatDocument()` applies document-level normalization.
5. The processor replaces the entire document only when the reformatted text differs.

## Core algorithm
`reformatIndentation()` is a single-pass line walker with a small amount of formatter state:

- `level`: current indentation depth. This acts like a flat stack counter for open Rexx blocks.
- `extraOneShot`: one-time extra indent applied only to the next printable line. It handles `THEN`, `ELSE`, and `OTHERWISE` bodies when there is no trailing `DO`.
- `inBlockComment`: keeps multiline `/* ... */` comments aligned without trying to parse nested statement structure inside comments.
- `unit`: indent token generated from `indentSize` spaces.

The loop normalizes each incoming line, determines whether indentation should decrease before printing, emits the line, and then updates state for the next line.

## Helper functions
- `needsRexxFirstLineComment(text)`: returns `true` when the first non-empty line is not a block comment.
- `ensureRexxFirstLineComment(text)`: inserts the mandatory first-line block comment when missing.
- `firstEffectiveKeyword(line)`: extracts the first keyword on a line while skipping a leading label prefix.
- `lineEndsWithDo(line)`: detects trailing `DO` after stripping inline block comments.
- `lineEndsWithThen(line)`: detects single-statement `THEN` lines.
- `noInlineBody(line, keyword)`: checks whether `ELSE` or `OTHERWISE` already has a body on the same line.
- `isLabelLine(line)`: recognizes a pure label such as `main:`.
- `mergeEmptyLineBetweenSingleLineBlockComments(text)`: merges comment clusters without deleting intentional separation when there are two or more blank lines.
- `normalizeFileEnding(text)`: removes trailing blank lines and enforces one final newline.
- `normalizeWhitespace(line, indentSize)`: expands tabs, trims trailing spaces, and normalizes inline block-comment spacing.
- `normalizeCommentSpacing(line)`: compresses repeated spaces inside block comments.

## Implemented rules
Rule numbering follows the formatter rule language used in the recorded sessions.

### File-level rules
- **Rule 1.1** — ensure the first non-empty line is a block comment by injecting `REXX_FIRST_LINE_COMMENT` when needed.
- **Rule 1.3** — enforce exactly one trailing newline at end of file.

### Indentation rules
- **Rule 2.2** — de-indent `END` before emitting the line.
- **Rule 2.3** — indent bodies of standalone `DO` blocks.
- **Rule 2.4** — indent `IF ... THEN DO` blocks and also indent the next single statement after `THEN` when there is no `DO`.
- **Rule 2.5** — indent the body after a standalone `ELSE`.
- **Rule 2.6** — indent `SELECT`, `WHEN`, and `OTHERWISE` bodies, including single-statement `WHEN ... THEN` cases.

### Whitespace and blank-line rules
- **Rule 3.4** — collapse runs of blank lines to at most one empty line.

### Label / procedure rules
- **Rule 7.1** — keep labels at column 0 and indent the following procedure body at level 1.
- **Rule 8.1** — treat `RETURN` and `EXIT` as closing the label-opened procedure block after printing the statement.

## Configuration behavior
- Default formatter indent is **4 spaces**.
- IntelliJ Code Style can override the indent width.
- Any configured indent below 1 is coerced upward with `coerceAtLeast(1)`.
- The formatter does not currently expose custom Rexx formatting toggles beyond standard indent size.

## Test coverage summary
Formatter behavior is covered by `/Users/tymoshv/MyPetProjects/rexx-jetbrains-plugin/src/test/kotlin/org/lang/rexx/RexxFormattingTest.kt`.

Covered scenarios:
- mandatory first-line comment insertion and preservation
- `DO` / `END` indentation
- nested blocks
- `SELECT` / `WHEN` / `OTHERWISE`
- `THEN` without `DO`
- `IF` / `THEN` / `ELSE`
- label body indentation
- blank-line collapsing
- comment preservation and multiline block comments
- tab expansion, trailing-space cleanup, and block-comment spacing normalization
- adjacent single-line comment merging
- single trailing newline normalization for both LF and CRLF inputs

**Current count:** 23 formatter-focused tests.

## Known limitations
- No PSI-aware formatting yet; control flow is inferred from line text only.
- No continuation-line indentation rule for long expressions (**Rule 2.7** backlog item).
- No operator spacing (**Rule 3.1**) or comma spacing (**Rule 3.2**) normalization yet.
- No configurable keyword casing policy.
- Label handling is heuristic and assumes `label:` lines are standalone procedure starters.
- Inline comment handling only reasons about `/* ... */`; it does not introduce full Rexx syntax awareness.
