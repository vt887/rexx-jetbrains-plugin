# Roadmap

## Phase status

| Phase | Status | Notes |
|---|---|---|
| Phase 1 — Requirements | ✅ Complete | `docs/sdlc/00-project-vision.md` and `docs/sdlc/01-requirements.md` exist and define MVP scope, targets, extensions, and non-goals. |
| Phase 2 — Architecture | ✅ Complete | `docs/sdlc/02-architecture.md` and `docs/sdlc/03-agent-roles.md` exist; the repository now also has formatter and linter architecture notes under `docs/architecture/`. |
| Phase 3 — MVP Implementation | ✅ Complete | Core MVP is present: language/file type registration, lexer, token types, syntax highlighting, color settings page, samples, and tests. The repo also already includes post-MVP additions such as completion, a conservative formatter, comment-tag annotator, and a run configuration stack. |
| Phase 4 — Validation | ✅ Complete | `Makefile` exposes `build`, `test`, `verify`, `package`, and `runIde`; CI runs lint/build/test/verify/package; platform-backed test fixes removed the recorded instability. Current baseline on 2026-05-16: `./gradlew test --no-daemon` passes. |
| Phase 5 — Hardening | 🔄 In Progress | Security and compatibility reviews exist, ktlint is integrated, and formatter rules improved, but hardening gaps remain around parser maturity, formatter completeness, deprecation monitoring, and broader regression coverage. |
| Phase 6 — Release Preparation | ⏳ Pending | Release plan and final summary documents exist, but Marketplace metadata, final packaging validation, and a fully closed release checklist still need explicit completion. |

## What is done now
- Lexer-first Rexx plugin scaffold is operational.
- `.rexx`, `.rex`, and `.rx` file types are registered.
- Syntax highlighting, color settings, completion, formatter, and run configuration support exist.
- CI and release workflows are standardized around `make`.
- Kotlin linting is enforced with ktlint.

## In-progress focus
- Harden formatter behavior while the project remains parser-light.
- Keep IntelliJ compatibility and security review artifacts current.
- Expand regression coverage around formatting, parser stubs, and run configuration behavior.

## Future features from AGENTS.md
- Grammar-Kit parser
- PSI tree
- formatter maturation
- inspections
- autocomplete expansion
- folding
- Rexx run configuration improvements
- debugger integration
- code navigation
- refactoring support
