# Backlog

## Parser and PSI
- Grammar-Kit parser generation instead of the current minimal parser scaffolding
- Real PSI tree and statement/expression element model
- PSI-aware formatter rewrite once parser contracts stabilize
- Better parser error recovery and fixtures

## Formatter
- Formatter settings UI
- Live preview for formatter/code style changes
- Configurable keyword casing
- Continuation lines indentation (**Rule 2.7**)
- Operator spacing (**Rule 3.1**)
- Comma spacing (**Rule 3.2**)
- More complete comment and mixed inline-body handling
- Fixture-based formatter regression corpus

## Editor intelligence
- Richer autocomplete beyond the current keyword set
- Inspections and quick-fixes
- Structure-aware code navigation
- Folding support
- Refactoring support

## Run and debug
- Debugger integration
- More interpreter-specific validation and UX polish
- Execution environment presets / templates

## Hardening and release
- Broader deprecated API review as IntelliJ platform versions advance
- Additional compatibility matrix validation for IDEA and PyCharm
- Manual `runIde` smoke checklist for release candidates
- Marketplace metadata finalization
- Release checklist closure against `AGENTS.md`
