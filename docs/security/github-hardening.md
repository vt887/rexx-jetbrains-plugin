# GitHub Security Hardening Audit

## Scope

This audit covers repository-level GitHub hardening for `vt887/rexx-jetbrains-plugin` with emphasis on branch protection, workflow permissions, dependency update automation, repository security metadata, and baseline code scanning.

Audit date: 2026-05-16

## Repository Analysis

### Repository profile
- Visibility: public
- Default branch: `main`
- Viewer permission used for this audit: `ADMIN`
- Primary language and build stack: Kotlin with Gradle Kotlin DSL
- Main package ecosystem detected: Gradle
- Automation ecosystem detected: GitHub Actions

### CI/CD inventory
- `.github/workflows/ci.yml`
  - runs on `push`, `pull_request`, and manual dispatch
  - performs lint, build, test, verification, and packaging
- `.github/workflows/release.yml`
  - runs on version tags and manual dispatch
  - builds, verifies, packages, and publishes a GitHub Release

### Existing GitHub security state observed before hardening
- `main` branch protection: not configured
- GitHub Rulesets: not configured
- Dependabot security updates: enabled
- Secret scanning: enabled
- Secret scanning push protection: enabled
- Secret scanning non-provider patterns: disabled
- Secret scanning validity checks: disabled

## Implemented Repository Changes

### 1. Dependabot baseline
Added weekly Dependabot updates for:
- Gradle dependencies
- GitHub Actions

Why this is needed:
- keeps build and automation dependencies moving without excessive PR noise
- groups minor and patch updates to reduce operational overhead
- complements already enabled Dependabot security updates

### 2. Security policy
Added `SECURITY.md`.

Why this is needed:
- establishes a supported-version policy
- defines a non-public vulnerability reporting path
- reduces the chance of sensitive details being disclosed in public issues

### 3. Ownership metadata
Added `.github/CODEOWNERS`.

Why this is needed:
- gives a default reviewer/owner baseline for future team growth
- makes security-sensitive paths explicit
- supports review enforcement once branch protection or rulesets require reviews

### 4. PR security checklist
Added `.github/pull_request_template.md`.

Why this is needed:
- makes contributors verify minimum workflow permission and secret-handling concerns before merge
- creates a lightweight review baseline for solo work and future collaborators

### 5. Issue reporting guidance
Added `.github/ISSUE_TEMPLATE/config.yml` with a link to `SECURITY.md`.

Why this is needed:
- nudges users away from public disclosure of vulnerabilities
- improves discoverability of the security policy

### 6. Code scanning baseline
Added `.github/workflows/codeql.yml`.

Why this is needed:
- enables automated static analysis for the repository's supported language stack
- provides a maintained baseline scanner integrated with GitHub code scanning
- ensures scanning runs on pull requests to `main`, direct pushes to `main`, and a weekly schedule

### 7. GitHub Actions permission tightening
Updated existing workflow expectations to a minimum-permission model:
- CI keeps top-level `contents: read`
- Release workflow now uses top-level `contents: read` and job-level `contents: write`

Why this is needed:
- narrows repository token exposure for jobs that do not need write access
- keeps release publishing functional while reducing ambient write permission

## Live GitHub Settings Applied

The following repository settings were applied directly through the GitHub API during this hardening pass:

- classic branch protection enabled for `main`
- pull requests required before merge
- 1 approving review required
- required status checks enabled:
  - `Build & Test`
  - `Verify Plugin`
  - `Package Plugin`
- branch must be up to date before merge
- conversation resolution required before merge
- force pushes to `main` blocked
- deletion of `main` blocked

Intentional solo-maintainer exception:
- admin enforcement remains disabled to avoid locking the sole maintainer out of merges that require an external approval

Already enabled before this pass:
- Dependabot security updates
- secret scanning
- secret scanning push protection
- private vulnerability reporting

Attempted but not applied through API:
- `secret_scanning_non_provider_patterns`
- `secret_scanning_validity_checks`

Note:
- both optional secret-scanning settings were targeted through `PATCH /repos/{owner}/{repo}` with explicit API version headers
- GitHub accepted the request without an HTTP error but returned unchanged repository state
- treat both settings as availability-dependent or UI-gated for this repository

## Manual GitHub UI Settings Required

These settings remain recommended or conditional after the live hardening changes above.

### Prefer GitHub Rulesets if available
Create a branch ruleset targeting `main` with equivalent or stronger controls than the current classic branch protection:
- require pull request before merging
- require at least 1 approving review
- require conversation resolution before merge
- require status checks before merging
- require branches to be up to date before merging
- block force pushes
- block branch deletion
- optionally require signed commits if the maintainer workflow can support it consistently

Recommended required status checks once rulesets are applied:
- `Build & Test`
- `Verify Plugin`
- `Package Plugin`
- `Analyze (java-kotlin)`

Current note:
- classic branch protection is already active on `main`
- add `Analyze (java-kotlin)` after the CodeQL workflow is merged to `main` and has produced a real check run

### Additional GitHub security settings
Confirm or enable:
- dependency graph
- Dependabot alerts
- Dependabot security updates
- secret scanning
- secret scanning push protection
- private vulnerability reporting

Optional but recommended if available:
- secret scanning non-provider patterns
- secret scanning validity checks
- vigilant mode or repository-level signed commit expectations

Current note:
- an API attempt was made to enable `secret_scanning_non_provider_patterns` and `secret_scanning_validity_checks`
- GitHub returned unchanged state, so both remain manual or feature-availability dependent here

## Workflow Hardening Notes

### Current good baseline
- CI already runs on pull requests before merge
- workflows use maintained first-party actions and current major versions
- release publication is isolated to a separate workflow

### Remaining recommendations
- pin critical third-party and first-party actions to commit SHAs for stronger supply-chain integrity
- review whether `actions/cache` remains necessary once Gradle action caching is sufficient
- consider limiting release workflow execution to protected refs only if release cadence grows

## Final Checklist

### Implemented automatically
- [x] weekly Dependabot updates for Gradle and GitHub Actions
- [x] `SECURITY.md`
- [x] `.github/CODEOWNERS`
- [x] `.github/pull_request_template.md`
- [x] issue reporting guidance via `.github/ISSUE_TEMPLATE/config.yml`
- [x] CodeQL workflow for Kotlin/Gradle stack
- [x] release workflow permission tightening documented and applied
- [x] classic branch protection on `main`
- [x] pull requests required before merging
- [x] 1 approving review required
- [x] required status checks enabled for current CI jobs
- [x] branch up-to-date requirement enabled
- [x] force pushes blocked
- [x] branch deletion blocked
- [x] conversation resolution required
- [x] private vulnerability reporting already confirmed enabled

### Needs manual GitHub UI configuration
- [ ] migrate from classic branch protection to GitHub Rulesets if preferred
- [ ] add `Analyze (java-kotlin)` to required checks after CodeQL is merged and runs on `main`
- [ ] optionally require signed commits if practical

### Optional future improvements
- [ ] enable admin enforcement when there is a second maintainer or an approval workflow that will not block solo merges
- [ ] pin GitHub Actions to immutable SHAs
- [ ] enable secret scanning non-provider patterns
- [ ] enable secret scanning validity checks
- [ ] add a release-environment approval gate if more maintainers start publishing releases
- [ ] add a lightweight workflow for policy linting or actionlint in CI
