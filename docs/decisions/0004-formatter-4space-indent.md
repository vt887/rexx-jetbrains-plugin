# Decision: Formatter 4-space indent
**Date:** 2026-05-16
**Status:** Accepted

## Context
The formatter had been defaulting to 3-space indentation, but the supplied Rexx formatting rules required 4 spaces. The previous behavior also failed to indent label bodies and single-statement `THEN` branches correctly.

## Decision
Standardize the formatter default on 4 spaces, preserve user-configured indent widths through IntelliJ code style settings, and clamp invalid indent values with `coerceAtLeast(1)`. Complement this with targeted one-shot indentation logic for `THEN`, `ELSE`, and `OTHERWISE` bodies.

## Alternatives Considered
- Keep the 3-space historical default.
- Make indentation entirely fixed and ignore IDE code style settings.
- Defer all formatter corrections until a PSI-aware formatter exists.

## Consequences
Formatter output now matches the documented Rexx style more closely and tests encode the 4-space baseline. The formatter remains heuristic, so some advanced rules stay in backlog until parser / PSI support arrives.
