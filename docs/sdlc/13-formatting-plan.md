# Rexx Formatting Plan

## Current status
The formatter is intentionally conservative because the plugin is still lexer-only and does not yet have a parser or PSI model.

## Implemented rule
Before formatting, the plugin ensures that the first non-empty line is a Rexx comment. If it is missing, the formatter inserts:

```text
/* The first line of a REXX exec must always be a comment. */
```

## Current limitations
- no syntax-aware indentation
- no block alignment for `DO` / `END`, `IF` / `THEN`, or `SELECT` / `WHEN`
- no wrapping or spacing rules beyond the document-level guard
- formatting quality depends on future PSI availability

## Path to a full formatter
1. add Grammar-Kit parser support
2. introduce PSI element types for statements and expressions
3. replace the flat formatting block with statement-aware blocks
4. add configurable spacing, indentation, and wrapping policies
5. expand tests with formatter fixtures

## Style reference
Formatting work should follow established Rexx style guidance:
https://rexxinfo.org/info/articles/best_coding_practices_for_rexx.html
