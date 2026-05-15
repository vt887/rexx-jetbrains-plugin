# Security Review

## Scope

MVP language plugin: file type registration, lexical analysis, highlighting, editor color settings.

## Threat Model (MVP)

- Untrusted source files opened in IDE.
- Potential plugin misuse via file/process/network APIs.

## Findings

1. No runtime process execution in MVP classes.
2. No direct file writes or external network calls in MVP classes.
3. Plugin.xml currently uses minimal language-related extension points only.
4. No custom permissions or privileged actions required.

## Risks To Watch In Future Phases

- Run configuration support may introduce process execution risk.
- Formatter/settings that read external config may introduce unsafe file handling.
- Inspections using external tools may add command-injection surfaces.

## Hardening Checklist

- Keep all process launches explicit, validated, and user-initiated.
- Reject absolute-path assumptions and unsafe path traversal logic.
- Keep plugin.xml extensions minimal.
- Add tests for any command-building logic before enabling run support.
- Re-run API deprecation/security review each phase.
