# Decision: Makefile over direct Gradle
**Date:** 2026-05-16
**Status:** Accepted

## Context
Local development and GitHub Actions both needed to run the same Gradle lifecycle steps. The project also needed a single place to expose auxiliary tasks such as environment version reporting and later lint / format entrypoints.

## Decision
Introduce a top-level `Makefile` as the stable task façade for the repository. Local workflows and GitHub Actions should call `make` targets instead of hardcoding `./gradlew` command sequences in multiple places.

## Alternatives Considered
- Keep direct `./gradlew` invocations in workflow YAML and documentation.
- Add bespoke shell scripts per task instead of a standard `Makefile`.
- Rely only on IDE run configurations for common actions.

## Consequences
The repository now has one public command surface for build, test, verify, package, lint, format, and version diagnostics. Workflow YAML became thinner, but the `Makefile` is now part of the repository contract and must be maintained whenever build steps change.
