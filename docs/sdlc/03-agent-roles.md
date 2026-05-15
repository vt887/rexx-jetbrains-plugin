# Agent Roles

This document defines the responsibilities of each AI agent role in this project's SDLC.
See `AGENTS.md` for full model recommendations and workflow.

---

## Architect Agent
*Recommended: Claude, Gemini*

Responsibilities:
- Define plugin architecture and extension-point strategy.
- Validate package structure and IntelliJ Platform conventions.
- Review future PSI/parser scalability.
- Maintain `docs/sdlc/02-architecture.md` and `docs/sdlc/09-risk-register.md`.

Rules:
- Prefer simple architecture. No unnecessary abstractions.
- IntelliJ SDK conventions take precedence over generic Kotlin patterns.

---

## Implementation Agent
*Recommended: Codex, OpenCode*

Responsibilities:
- Implement Kotlin source files and Gradle configuration.
- Fix compilation errors and integration issues.
- Maintain small, logical, reviewable commits.

Rules:
- Keep classes small and readable. No dead code. No speculative abstractions.
- One logical concern per commit.

---

## Lexer & Parser Agent
*Recommended: Codex, Claude*

Responsibilities:
- Implement and maintain `RexxLexer`, token types, and token sets.
- Prepare future JFlex/Grammar-Kit migration path.
- Maintain lexer-level tests.

Rules:
- MVP first: stable tokenization before any PSI work.
- Parser implementation is Phase 2, not Phase 1.

---

## QA Agent
*Recommended: Codex, OpenCode*

Responsibilities:
- Write and maintain unit and integration tests.
- Validate plugin loading, lexer correctness, and syntax highlighting.
- Run `./gradlew clean build`, `./gradlew test`, `./gradlew runIde`.
- Maintain `docs/sdlc/05-test-strategy.md`.

Rules:
- Every implemented feature requires tests.
- Capture exact build/test failures. Fix root causes only.

---

## Security Agent
*Recommended: Gemini, Claude*

Responsibilities:
- Review plugin permissions and risky API usage.
- Check file/process execution risks.
- Maintain `docs/sdlc/06-security-review.md`.

Rules:
- No unsafe shell execution. No hardcoded secrets. No unnecessary permissions.

---

## Compatibility Agent
*Recommended: Gemini*

Responsibilities:
- Audit IntelliJ API usage for deprecated calls.
- Validate IDE compatibility matrix (`sinceBuild` / `untilBuild`).
- Maintain `docs/sdlc/07-compatibility-review.md`.

Rules:
- Prefer latest stable APIs. Maintain IDEA + PyCharm compatibility.

---

## Documentation Agent
*Recommended: Claude, Ollama*

Responsibilities:
- Maintain `README.md`, `CHANGELOG.md`, and all `docs/sdlc/` files.
- Ensure documentation matches implementation state.
- Avoid stale documentation.

---

## Release Agent
*Recommended: Claude*

Responsibilities:
- Prepare release checklist, changelog policy, and Marketplace metadata.
- Maintain `docs/sdlc/08-release-plan.md` and `docs/sdlc/10-final-summary.md`.

---

## Local Offline Assistant
*Recommended: Ollama (qwen2.5-coder, deepseek-coder, codellama)*

Responsibilities:
- Generate Rexx sample files and offline summaries.
- Draft simple documentation sections.
- Explain local code snippets.

Rules:
- Does not make final architectural or security decisions.
- Does not replace review agents.
