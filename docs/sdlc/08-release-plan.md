# Release Plan

## Release Objective

Ship a stable MVP Rexx language plugin artifact installable in IntelliJ IDEA and PyCharm.

## Pre-Release Checklist

1. Build/test/runIde commands pass.
2. README reflects actual implemented features and limitations.
3. CHANGELOG includes user-visible changes.
4. `plugin.xml` metadata validated.
5. Compatibility review and security review updated.
6. Sample files included and validated manually.

## Packaging

- Build plugin ZIP via `buildPlugin`.
- Validate ZIP in local IDE install flow.

## Marketplace Readiness (Phase 4)

- Finalize vendor and plugin IDs.
- Add compatibility matrix by IDE version.
- Add signed release and publication workflow.

## Rollback Strategy

- Keep versioned artifacts per release.
- If regression is found, roll back to previous ZIP and mark incompatible versions in release notes.
