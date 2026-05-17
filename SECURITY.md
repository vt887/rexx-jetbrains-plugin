# Security Policy

## Supported Versions

This repository is currently pre-`1.0`.

| Version | Supported |
| --- | --- |
| Latest `main` branch | Yes |
| Latest GitHub Release | Yes |
| Older tags and historic branches | No |

## Reporting a Vulnerability

Do not report exploitable vulnerabilities in public issues or pull requests.

Preferred reporting path:
1. Use GitHub private vulnerability reporting if it is enabled for this repository.
2. If private reporting is not available, contact the maintainer directly before disclosing technical details publicly.

When reporting, include:
- affected commit, tag, or branch
- reproduction steps
- impact assessment
- suggested mitigation if known

The maintainer will aim to:
- acknowledge receipt within 7 days
- confirm whether the issue is in scope
- coordinate a fix and disclosure plan when the report is valid

## Scope

Security-sensitive areas in this repository include:
- GitHub Actions workflows and release automation
- local process execution from the IntelliJ plugin
- dependency update and release supply chain
- plugin packaging and distribution artifacts
