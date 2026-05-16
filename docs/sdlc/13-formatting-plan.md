# Rexx Formatting Plan

## Current status

The formatter combines a `PreFormatProcessor` (text-level pass before the IntelliJ formatter runs)
with a minimal `FormattingModelBuilder` (structural placeholder). It has a minimal PSI scaffolding
(`RexxParserDefinition`, `RexxFile`, `RexxPsiElement`) but no semantic parser yet.

## Implemented rules

The following transformations are applied by `RexxPreFormatProcessor.reformatDocument`:

| Rule | Description |
|------|-------------|
| 1.1 | Inserts `/* The first line of a REXX exec must always be a comment. */` if the first non-empty line is not a block comment |
| 1.3 | Ensures a single trailing newline |
| 2.1–2.2 | 4-space indentation inside `DO` / `END` blocks |
| 2.3 | Each nesting level adds 4 spaces |
| 2.4–2.5 | `IF … THEN` without `DO`: next printable line is indented one level; `ELSE` / `OTHERWISE` resets to block level |
| 2.6 | `SELECT` / `WHEN` / `OTHERWISE` / `END` block indentation |
| 3.3 | Trailing whitespace removed from every line |
| 3.4 | Multiple consecutive blank lines collapsed to at most one |
| 5.2 | Comment text content is never modified (only indentation is adjusted) |
| 7.1 | Labels (single token ending with `:`) are placed at column 0; body is indented one level |

## Current limitations

- No operator/comma spacing (Rules 3.1, 3.2) — deferred to Phase 3
- No continuation-line alignment (Rule 2.7) — deferred to Phase 3
- No keyword casing normalization (Rule 4.1) — deferred to Phase 3
- No assignment alignment (Rule 11 future) — deferred to Phase 3
- Indentation is heuristic (text-based), not PSI-aware; edge cases may arise with complex nesting

## Path to a full formatter

1. Add Grammar-Kit / JFlex parser
2. Introduce PSI element types for statements and expressions
3. Replace the flat formatting block with statement-aware blocks
4. Add configurable spacing, indentation, and wrapping policies
5. Expand tests with formatter fixtures

## Style reference

Formatting work follows established Rexx style guidance:
https://rexxinfo.org/info/articles/best_coding_practices_for_rexx.html
