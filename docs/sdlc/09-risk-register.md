# Risk Register

## R1: Build Tooling Blocker

- Risk: Missing Gradle wrapper blocks deterministic validation.
- Impact: High
- Mitigation: Add Gradle wrapper and pin Gradle/JDK versions.
- Owner: Implementation Agent

## R2: Lexer Drift

- Risk: Handwritten lexer behavior diverges from future JFlex grammar.
- Impact: Medium
- Mitigation: Keep lexer tests as executable spec before JFlex migration.
- Owner: QA Agent

## R3: Compatibility Drift

- Risk: IntelliJ API changes break plugin on newer IDEs.
- Impact: Medium
- Mitigation: Compatibility review each phase and CI matrix later.
- Owner: Review Agent

## R4: Security Expansion In Later Phases

- Risk: Run configuration/formatter introduces unsafe process or file operations.
- Impact: High
- Mitigation: Security gates and tests before enabling execution features.
- Owner: Security Agent

## R5: Scope Creep Before MVP Stability

- Risk: Parser/PSI and DX features added before MVP is stable.
- Impact: Medium
- Mitigation: Enforce phase gates and done criteria.
- Owner: Architect Agent
