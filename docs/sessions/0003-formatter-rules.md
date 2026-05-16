# Session 0003 — Formatter rules
        **Date:** 2026-05-16

        ## Prompt
        ```text
        The user showed that `Reformat Code` was producing wrong output:
        - Extra trailing blank line added
        - Label body (`main:`) not indented
        - After `THEN`, next line not indented

        Full Rexx Formatting Rules were provided (Rules 1-11), key points:
        - 4 spaces indent (not 3)
        - Labels at column 0, body indented
        - IF/THEN without DO: next statement indented
        - ELSE body indented
        - SELECT/WHEN/OTHERWISE: WHEN body and OTHERWISE body indented
        - RETURN/EXIT close label-opened procedure block
        - Max 1 consecutive blank line (Rule 3.4)
        - File ends with single `
` (Rule 1.3)
        ```

        ## Summary
        - Changed the formatter default from 3 spaces to 4 spaces.
        - Added `extraOneShot` to indent the next printable line after `THEN`, `ELSE`, or `OTHERWISE` when there is no inline body.
        - Added label detection so labels stay at column 0 while their bodies start at indentation level 1.
        - Added `lineEndsWithThen()` and `noInlineBody()` helpers to separate `THEN DO` from single-statement bodies.
        - Treated `RETURN` and `EXIT` as closing a label-opened procedure block after the statement is emitted.
        - Collapsed consecutive blank lines to at most one and normalized the file ending to exactly one trailing newline.
        - Updated formatter tests from 3-space expectations to 4-space expectations and added targeted regressions for labels, `THEN`, `ELSE`, and blank lines.

        ## Files changed
        - `/Users/tymoshv/MyPetProjects/rexx-jetbrains-plugin/src/main/kotlin/org/lang/rexx/format/RexxPreFormatProcessor.kt`
        - `/Users/tymoshv/MyPetProjects/rexx-jetbrains-plugin/src/test/kotlin/org/lang/rexx/RexxFormattingTest.kt`

        ## Test results
        - Historical session result: exact pass/fail totals were not included in the provided history.
        - Scope of regression updates: 4 new formatter tests were added and existing formatter expectations were updated from 3-space to 4-space indentation.
        - Current repository baseline on 2026-05-16: `./gradlew test --no-daemon` → 55 passed, 0 failed, 0 errors, 0 skipped.

        ## Architecture decisions made
        - Keep the formatter conservative and line-oriented until a real parser / PSI model exists.
        - Standardize on 4-space indentation as the default Rexx formatting policy.
        - Encode single-line body indentation as a one-shot state transition instead of trying to infer full block structure without PSI.
