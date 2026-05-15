# Rexx Language Notes

This plugin currently implements lexical support only. It intentionally avoids PSI and parser commitments until the token model is stable.

## Implemented Tokens

- Block comments: `/* comment */`, including nested comments.
- Strings: single-quoted and double-quoted strings, with doubled quotes as escapes.
- Numbers: integers, decimals, and simple exponent notation such as `42.5E+2`.
- Symbols: Rexx-style symbols such as `wordin`, `stem.1`, `queue_name`, and `!flag`.
- Operators: arithmetic, concatenation, comparison, and Rexx not-equal variants such as `\=`.
- Keywords: core control and parse keywords, matched case-insensitively.

## Future Parser Requirements

- Model clauses and statement boundaries.
- Distinguish instructions from built-in functions and user symbols.
- Add PSI for labels, procedures, variable references, compound stems, and parse templates.
- Add Grammar-Kit BNF once the lexer behavior is covered by fixture tests.
