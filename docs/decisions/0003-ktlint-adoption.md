# Decision: ktlint adoption
**Date:** 2026-05-16
**Status:** Accepted

## Context
Kotlin formatting had started to drift across both production and test sources, and there was no automated style gate in CI. The repository needed a simple, predictable way to check and auto-fix Kotlin formatting without introducing a large new toolchain.

## Decision
Adopt `ktlint 1.8.0` as the Kotlin style checker and formatter, expose it through `make lint` and `make format`, and run lint in CI before build steps.

## Alternatives Considered
- Depend on IDE-local formatting only.
- Add a heavier analysis stack such as detekt for formatting enforcement.
- Postpone linting until after parser / PSI work.

## Consequences
Kotlin style is now enforced automatically and can block CI early. Contributors must either follow the standard ktlint ruleset or document narrowly scoped suppressions when a rule exception is justified.
