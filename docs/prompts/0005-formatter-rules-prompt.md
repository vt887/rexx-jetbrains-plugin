# Prompt 0005 — Formatter rules
        **Session:** 0003 — Formatter rules
        **Date:** 2026-05-16

        ## Exact prompt
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

        ## Context
        Formatter behavior was corrected against a user-provided Rexx style specification and concrete bad-output examples.
