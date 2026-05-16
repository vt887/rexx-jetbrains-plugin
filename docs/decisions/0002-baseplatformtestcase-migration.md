# Decision: BasePlatformTestCase migration
**Date:** 2026-05-16
**Status:** Accepted

## Context
Several tests started failing because IntelliJ Platform classes such as `Language` were initialized outside a running application context. Parallel classloading also exposed duplicate `Language("Rexx")` registration conflicts.

## Decision
Enable the IntelliJ Platform test framework in Gradle and migrate platform-coupled tests to `BasePlatformTestCase`. Tests that reference language registration, token types, syntax highlighting, or completion infrastructure should use platform-backed test fixtures by default.

## Alternatives Considered
- Keep pure JUnit tests and attempt to mock IntelliJ internals.
- Serialize the failing tests without changing the test framework.
- Refactor production singletons to avoid early class initialization entirely.

## Consequences
Test execution becomes slightly heavier, but platform state is initialized correctly and test behavior matches real IDE runtime assumptions. The repository now has a clear boundary: lightweight tests are allowed only when they do not depend on IntelliJ application services or language registration.
