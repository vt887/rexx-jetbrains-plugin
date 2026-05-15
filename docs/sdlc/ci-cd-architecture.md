# CI/CD Architecture

## Overview

Two workflows cover the full delivery lifecycle:

| Workflow | File | Trigger |
|---|---|---|
| CI | `.github/workflows/ci.yml` | PR, push to main/master/develop, manual |
| Release | `.github/workflows/release.yml` | `v*` tags, manual |

---

## CI Pipeline (`ci.yml`)

```
push / PR
    │
    ▼
[build]  →  compile + test (fail fast)
    │
    ▼
[verify] →  verifyPlugin (plugin.xml + archive structure)
    │
    ▼
[package] → buildPlugin → upload ZIP artifact (30-day retention)
```

### Stage details

| Stage | Gradle task(s) | Failure behaviour |
|---|---|---|
| Build & Test | `clean build -x test`, `test` | Uploads test reports on failure |
| Verify Plugin | `verifyPlugin` | Blocks package stage |
| Package | `buildPlugin` | Uploads ZIP; errors if no ZIP found |

### Caching strategy

- **Gradle home** — managed by `gradle/actions/setup-gradle` (configuration cache + dependency cache).
- **IntelliJ SDK** — separate `actions/cache` key on `build.gradle.kts` hash. Avoids re-downloading the ~800 MB IDE bundle on every run.
- Cache writes are restricted to `main`/`master` branches; PRs are cache-read-only.

### Concurrency

`cancel-in-progress: true` — outdated runs on the same branch/PR are cancelled automatically.

---

## Release Pipeline (`release.yml`)

```
git tag v*  (or workflow_dispatch)
    │
    ▼
build + test + verifyPlugin + buildPlugin
    │
    ▼
upload artifact (90-day retention)
    │
    ▼
create GitHub Release + attach ZIP
```

- `cancel-in-progress: false` — releases are never cancelled mid-run.
- `prerelease: true` is set automatically for tags containing `-` (e.g. `v0.2.0-beta`).

---

## Artifact Flow

```
./gradlew buildPlugin
    └── build/distributions/idea-rexx-plugin-<version>.zip
            │
            ├── CI:      actions/upload-artifact  → GitHub Actions artifacts (30 days)
            └── Release: GitHub Release asset     → permanent download on release page
```

---

## Permissions

| Workflow | Permission | Reason |
|---|---|---|
| CI | `contents: read`, `checks: write` | Read source, post check results |
| Release | `contents: write` | Create GitHub Release + upload assets |

No secrets required for CI. Release uses the default `GITHUB_TOKEN`.

---

## How to Test Locally

```bash
# Replicate CI build stage
./gradlew clean build -x test --no-daemon

# Replicate test stage
./gradlew test --no-daemon

# Replicate verify stage
./gradlew verifyPlugin --no-daemon

# Replicate package stage
./gradlew buildPlugin --no-daemon
ls build/distributions/
```

---

## How to Trigger a Release

```bash
git tag v0.1.0
git push origin v0.1.0
```

The release workflow runs automatically and creates a GitHub Release with the plugin ZIP attached.
