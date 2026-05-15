# Compatibility Review

## Target IDEs

- IntelliJ IDEA
- PyCharm

## MVP Compatibility Posture

- Plugin depends on `com.intellij.modules.lang`, not IDEA-only module APIs.
- Language support path uses stable extension points for file type and syntax highlighting.
- No platform-specific native integrations.

## Deprecated API Check Plan

- Inspect imports for deprecated IntelliJ classes each phase.
- Build against current IntelliJ Platform Gradle Plugin line.
- Pin baseline IntelliJ target and review when upgrading.

## Extension Compatibility

- Supported extensions in MVP target: `.rexx`, `.rex`, `.rx`.
- Keep extension registration strict; avoid unexpected file-type capture.

## Edge Cases

- Mixed-case keywords are expected and supported.
- Nested comments and escaped string quotes are supported lexically.
- Parser-dependent correctness is out of scope until Phase 2.

## Recommendations

- Add compatibility matrix in release artifacts for tested IDE builds.
- Add CI matrix once wrapper and CI pipeline are available.
