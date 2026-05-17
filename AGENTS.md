# AGENTS.md

# Rexx JetBrains Plugin — Multi-Agent SDLC Configuration

## Project

Project name:
`rexx-jetbrains-plugin`

Goal:
Develop a production-quality IntelliJ Platform plugin for the Rexx programming language compatible with:
- IntelliJ IDEA
- PyCharm

Primary stack:
- Kotlin
- Gradle Kotlin DSL
- IntelliJ Platform SDK
- JFlex
- Grammar-Kit (future phases)

---

# Core SDLC Principles

1. The repository must remain buildable after every major change.
2. Do not implement complex PSI/parser logic before MVP stabilization.
3. Prefer incremental commits over large patches.
4. All architectural decisions must be documented.
5. All implemented functionality must have tests.
6. Avoid deprecated IntelliJ APIs.
7. Keep code modular and maintainable.
8. Use minimal abstractions.
9. Documentation is mandatory.
10. Never overwrite existing files without review.
11. After every code change, run `make lint`, `make build`, and `make test`.

---

# Repository Structure

```text
docs/
  sdlc/

src/
  main/
  test/

testData/

.gradle/
.idea/
```

---

# AI Agent Roles

## 1. Architect Agent

Recommended model:
- Claude
- Gemini

Responsibilities:
- Define overall plugin architecture
- Review package structure
- Validate IntelliJ extension point usage
- Review maintainability
- Review future PSI/parser scalability
- Produce architecture documentation

Main outputs:
- docs/sdlc/02-architecture.md
- docs/sdlc/09-risk-register.md

Rules:
- Prefer simple architecture
- Avoid unnecessary abstractions
- Prioritize IntelliJ SDK conventions

---

## 2. Implementation Agent

Recommended model:
- Codex
- OpenCode

Responsibilities:
- Implement Kotlin code
- Create Gradle configuration
- Implement IntelliJ plugin classes
- Fix compilation errors
- Maintain small logical commits

Main outputs:
- Kotlin source files
- Gradle files
- plugin.xml
- lexer/highlighter implementation

Rules:
- Keep classes small
- Prefer readability
- Avoid dead code
- No speculative abstractions

---

## 3. Lexer & Parser Agent

Recommended model:
- Codex
- Claude

Responsibilities:
- Implement lexer
- Define token types
- Prepare future JFlex integration
- Prepare future Grammar-Kit migration

Main outputs:
- RexxLexer
- RexxTokenType
- RexxTokenSets
- lexer tests

Rules:
- MVP first
- Stable tokenization before PSI
- Parser implementation is NOT Phase 1

---

## 4. QA Agent

Recommended model:
- Codex
- OpenCode

Responsibilities:
- Implement tests
- Run Gradle validation
- Validate plugin loading
- Validate syntax highlighting
- Validate lexer correctness

Required commands:
```bash
make lint
make build
make test
./gradlew runIde
```

Main outputs:
- unit tests
- integration tests
- docs/sdlc/05-test-strategy.md

Rules:
- Every implemented feature requires tests
- Capture exact build/test failures
- Fix root causes only

---

## 5. Security Agent

Recommended model:
- Gemini
- Claude

Responsibilities:
- Review plugin security
- Review unsafe process execution
- Review file operations
- Review plugin permissions
- Review interpreter execution risks

Main outputs:
- docs/sdlc/06-security-review.md

Rules:
- No unsafe shell execution
- No hardcoded secrets
- No unnecessary permissions
- No insecure temporary file handling

---

## 6. Compatibility Agent

Recommended model:
- Gemini

Responsibilities:
- Review IntelliJ API compatibility
- Check deprecated APIs
- Validate IDE compatibility matrix
- Validate Gradle compatibility

Main outputs:
- docs/sdlc/07-compatibility-review.md

Rules:
- Prefer latest stable APIs
- Maintain IDEA + PyCharm compatibility

---

## 7. Documentation Agent

Recommended model:
- Claude
- Ollama

Responsibilities:
- Maintain README.md
- Maintain SDLC documents
- Document architecture decisions
- Create examples

Main outputs:
- README.md
- docs/sdlc/*.md

Rules:
- Documentation must match implementation
- Avoid stale documentation

---

## 8. Local Offline Assistant

Recommended model:
- Ollama

Responsibilities:
- Generate Rexx examples
- Summarize repository state
- Draft documentation
- Explain local code

Rules:
- Do not make final architectural decisions
- Do not replace review agents
- Use primarily for offline/local tasks

Recommended local models:
- qwen2.5-coder
- deepseek-coder
- codellama
- gemma3
- mistral-small

---

# SDLC Workflow

## Phase 1 — Requirements

Required artifacts:
- docs/sdlc/00-project-vision.md
- docs/sdlc/01-requirements.md

Tasks:
- Define MVP scope
- Define supported IDEs
- Define supported file extensions
- Define non-goals

---

## Phase 2 — Architecture

Required artifacts:
- docs/sdlc/02-architecture.md
- docs/sdlc/03-agent-roles.md

Tasks:
- Define package structure
- Define extension points
- Define lexer/highlighter architecture
- Define future parser migration strategy

---

## Phase 3 — MVP Implementation

Required functionality:
- Language class
- FileType
- syntax highlighting
- lexer
- token types
- comments
- strings
- operators
- keywords
- color settings page
- sample Rexx files
- tests

Rules:
- No PSI yet
- No formatter yet
- No interpreter integration yet

---

## Phase 4 — Validation

Required commands:
```bash
make lint
make build
make test
./gradlew runIde
```

Tasks:
- Fix all compilation errors
- Validate plugin startup
- Validate highlighting behavior

---

## Phase 5 — Hardening

Tasks:
- Remove deprecated APIs
- Review plugin.xml
- Review extension registrations
- Review code quality
- Review security

Required artifacts:
- docs/sdlc/06-security-review.md
- docs/sdlc/07-compatibility-review.md

---

## Phase 6 — Release Preparation

Required artifacts:
- docs/sdlc/08-release-plan.md
- docs/sdlc/10-final-summary.md

Tasks:
- Prepare README
- Prepare changelog
- Prepare Marketplace metadata
- Validate plugin packaging

---

# Coding Standards

## Kotlin

Rules:
- Use idiomatic Kotlin
- Prefer immutable values
- Prefer expression bodies where readable
- Avoid giant classes
- Avoid unnecessary inheritance

---

## IntelliJ Plugin Rules

- Use modern IntelliJ SDK APIs
- Avoid deprecated APIs
- Keep extension registrations minimal
- Prefer explicit registrations

---

## Testing Rules

- Tests are mandatory
- Use focused tests
- Avoid brittle snapshot tests
- Keep tests deterministic

---

# Commit Strategy

**Commit Approval Rule (mandatory):**
Before every `git commit`, the agent MUST:
1. Show a summary of all staged changes (`git status` + brief description)
2. Ask the user for explicit confirmation via `ask_user`
3. Only proceed with commit + push after user approval

This rule applies to ALL agents, ALL phases, NO exceptions.

Preferred commit format:
```text
type(scope): short description
```

Examples:
```text
feat(lexer): add Rexx keyword tokenization
fix(highlighter): correct string highlighting
test(filetype): add file registration tests
docs(architecture): add lexer design notes
```

---

# Forbidden Actions

Agents MUST NOT:
- Introduce deprecated IntelliJ APIs
- Add unnecessary frameworks
- Implement PSI too early
- Add unsafe shell execution
- Commit generated IDE files (e.g. `.intellijPlatform/`, `.gradle/`, `build/`)
- Create giant monolithic classes
- Skip documentation
- Skip tests
- Commit or push without explicit user approval (see Commit Strategy)

---

# Recommended Development Order

1. Gradle setup
2. plugin.xml
3. Language/FileType
4. Icons
5. Token types
6. Lexer
7. Syntax highlighter
8. Color settings page
9. Sample files
10. Tests
11. Documentation
12. Validation

---

# Recommended Future Features

After MVP stabilization:
- Grammar-Kit parser
- PSI tree
- formatter
- inspections
- autocomplete
- folding
- Rexx run configuration
- debugger integration
- code navigation
- refactoring support

---

# Final Validation Checklist

Before release:
- [ ] build passes
- [ ] tests pass
- [ ] runIde works
- [ ] highlighting works
- [ ] no deprecated APIs
- [ ] documentation updated
- [ ] README updated
- [ ] plugin.xml validated
- [ ] Marketplace metadata prepared

---

# Agent Instructions

<!-- lean-ctx -->
## lean-ctx

Prefer lean-ctx MCP tools over native equivalents for token savings.
Full rules: @LEAN-CTX.md
<!-- /lean-ctx -->
# End of AGENTS.md
