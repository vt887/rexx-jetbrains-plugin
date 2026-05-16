# Phase 2 Implementation Plan

## Overview
Phase 2 adds three editor and execution features on top of the lexer-only MVP:
- keyword completion for core Rexx control-flow statements
- a basic Rexx run configuration for interpreter-based execution
- formatter integration that enables safe `Reformat Code` participation

## Implemented
- `RexxCompletionContributor` with case-insensitive keyword completion and comment/string exclusions
- `RexxRunConfiguration` family for interpreter, script, working directory, and argument setup
- formatter registration, code style settings integration, and a pre-format first-line comment rule
- focused unit tests for completion, run configuration helpers, and formatting helpers

## Deferred
- PSI-aware completion and context ranking
- run configuration producer from the current editor/file
- interpreter autodetection and validation against specific Rexx runtimes
- structural formatter rules for indentation, spacing, and block alignment
- parser-backed formatting once Grammar-Kit/PSI is introduced
