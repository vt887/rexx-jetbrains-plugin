# Rexx Completion

## Keyword list
The current contributor suggests these Rexx keywords:

`ADDRESS`, `ARG`, `CALL`, `DO`, `DROP`, `ELSE`, `END`, `EXIT`, `IF`, `INTERPRET`, `ITERATE`, `LEAVE`, `NOP`, `NUMERIC`, `OTHERWISE`, `PARSE`, `PROCEDURE`, `PULL`, `PUSH`, `QUEUE`, `RETURN`, `SAY`, `SELECT`, `SIGNAL`, `THEN`, `TRACE`, `WHEN`, `WHILE`

## How completion works
- completion is registered for the Rexx language
- suggestions are added through `RexxCompletionContributor`
- matching is case-insensitive
- important control-flow entries include short tail text hints

## Context exclusions
Keyword completion is intentionally suppressed when the caret is inside:
- block comments
- string literals

## Next steps
- parser-aware context completion
- statement template completion (`DO ... END`, `IF ... THEN`)
- completion ranking based on surrounding tokens
- file-level symbols and labels once PSI exists
