# Test Strategy

## Test Levels

- Unit tests (primary for MVP):
  - lexer tokenization correctness
  - highlighter mapping correctness
  - color settings descriptors/presentation sanity
- Manual smoke tests:
  - open sample `.rexx`, `.rex`, `.rx` files in sandbox IDE
  - verify highlighting and color settings UI

## Coverage Matrix

- File type registration: plugin.xml + manual sandbox check.
- Lexer:
  - keywords
  - comments (nested)
  - strings (escaped quotes)
  - numbers
  - operators
  - punctuation
- Highlighter:
  - token type to `TextAttributesKey` mapping.
- Color settings page:
  - demo text + descriptor mapping.

## Execution Commands

- `./gradlew clean build`
- `./gradlew test`
- `./gradlew runIde`

## Pass/Fail Criteria

- Build and tests pass.
- Plugin starts in sandbox IDE.
- Supported extension files are recognized and highlighted.

## Current Limitations

- Execution is blocked until Gradle wrapper exists in repository.
