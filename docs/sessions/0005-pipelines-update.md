# Session 0005: Pipelines Update

## Prompt
- `docs/prompts/0007-create-pipelines-prompt.md`

## Summary
- Updated GitHub Actions pipelines in `rexx-jetbrains-plugin`.
- Removed `sudo` dependency from ktlint setup in CI and release workflows.
- Added a dedicated nightly regression workflow.

## Changed Files
- `.github/workflows/ci.yml`
- `.github/workflows/release.yml`
- `.github/workflows/nightly.yml`
- `docs/prompts/0007-create-pipelines-prompt.md`
- `docs/sessions/0005-pipelines-update.md`
- `docs/sdlc/ci-cd-architecture.md`

## Decisions
- Keep existing CI/release structure and improve reliability with local ktlint binary execution (`make lint KTLINT=./ktlint`).
- Add scheduled nightly checks for early drift detection.

## Risks
- Nightly pipeline depends on external downloads (ktlint + Gradle/intellij artifacts).
- `verifyPlugin` may be slower or flaky due to upstream IDE artifact availability.

## Validation
- Workflow YAML files created/updated.
- Manual structure review completed.

## Next Recommendations
- Add branch protection requiring `CI` workflow status.
- Optionally split `CI` into reusable workflow components.
