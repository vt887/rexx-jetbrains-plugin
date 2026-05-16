# Prompt 0004 — CI test failures
**Session:** 0002 — Test fixes
**Date:** 2026-05-16

## Exact prompt
```text
Full CI test output showing 13 failing tests:
- `RexxCompletionTest > skipsCompletionInsideCommentsAndStrings` — `ImplementationConflictException`
- `RexxLexerTest` (5/6 methods) — `NoClassDefFoundError` caused by `ExceptionInInitializerError` in `Language.java:105`
- `RexxSyntaxHighlighterTest` (7/15 methods) — `NoClassDefFoundError` from `RexxTokenTypes.kt:15`
```

## Context
Failure triage request based on GitHub Actions output after IntelliJ Platform classes started being exercised in tests.
