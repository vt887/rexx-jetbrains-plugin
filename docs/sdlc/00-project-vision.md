# Project Vision

## Project

`rexx-jetbrains-plugin` is a production-oriented IntelliJ Platform plugin that provides Rexx language support for IntelliJ IDEA and PyCharm.

## Problem Statement

Rexx codebases currently have weak IDE support in JetBrains products. Developers need baseline language ergonomics: file recognition, readable syntax, and predictable editor behavior.

## Vision

Deliver a maintainable plugin in incremental phases:

1. MVP language support (safe, stable lexical/editor features).
2. Language intelligence (parser/PSI-driven capabilities).
3. Developer experience (formatting, templates, run config, settings).
4. Release readiness for JetBrains Marketplace.

## Success Criteria

- Rexx files (`.rexx`, `.rex`, `.rx`) are recognized and highlighted.
- Plugin loads in IntelliJ IDEA and PyCharm without deprecated API usage.
- MVP features are covered by automated tests.
- SDLC documentation in `docs/sdlc/` is complete and actionable.
