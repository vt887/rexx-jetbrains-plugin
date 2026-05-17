# rexxlint plugin bridge

## Goal

The IntelliJ plugin does not implement Rexx linting or formatting rules in Kotlin.
`rexxlint` is the single source of truth. The plugin only discovers the executable,
invokes it through IntelliJ process APIs, parses machine-readable results, and maps
those results into IntelliJ diagnostics and formatting flows.

## Components

- `RexxlintSettingsState`: persists the optional user-configured executable path.
- `RexxlintConfigurable`: exposes **Settings | Tools | Rexx Lint**.
- `RexxlintExecutableLocator`: resolves `rexxlint` from the configured path, `PATH`, or common install locations.
- `RexxlintBridge`: builds `GeneralCommandLine`, sends source through stdin, enforces timeout handling, and returns structured success/failure results.
- `RexxlintJsonParser`: converts `rexxlint check --stdin --output json` output into IntelliJ-facing diagnostics.
- `RexxlintExternalAnnotator`: runs linting in the annotator pipeline and maps diagnostics to editor annotations.
- `RexxPreFormatProcessor`: delegates **Reformat Code** to `rexxlint format --stdin`.

## Command contract

Lint:

```bash
rexxlint check --stdin --output json
```

Format:

```bash
rexxlint format --stdin
```

The plugin may additionally pass `--path <current file>` when a file path is available so
`rexxlint` can apply path-sensitive behavior without re-reading from disk.

## Executable discovery order

1. Explicit path from settings.
2. First `rexxlint` found on `PATH`.
3. Common install locations.

Current common install locations:

- macOS: `/opt/homebrew/bin/rexxlint`, `/usr/local/bin/rexxlint`, `/usr/bin/rexxlint`
- Linux: `/usr/local/bin/rexxlint`, `/usr/bin/rexxlint`, `/snap/bin/rexxlint`
- Windows: `C:\Program Files\rexxlint\rexxlint.exe`, `C:\Program Files (x86)\rexxlint\rexxlint.exe`, `%LOCALAPPDATA%\Programs\rexxlint\rexxlint.exe`

## Error handling

- Missing executable: file-level warning annotation for lint runs; warning notification for formatting runs.
- Timeout: the process is forcibly terminated and reported as a graceful failure.
- Non-zero exit: stderr is surfaced as the primary user-facing message.
- Invalid JSON: the plugin reports an invalid-output warning instead of attempting partial interpretation.

## Manual validation

1. Install or build `rexxlint` and confirm `rexxlint --version` works.
2. Open **Settings | Tools | Rexx Lint** and set the executable path if `PATH` discovery is insufficient.
3. Open a `.rexx`, `.rex`, or `.rx` file with a known lint issue and verify editor annotations appear.
4. Run **Code | Reformat Code** and verify the file contents are replaced by `rexxlint format --stdin` output.
5. Temporarily point the setting at a non-existent binary and verify the plugin shows a graceful warning instead of throwing.
6. Simulate a long-running `rexxlint` build and verify timeout handling after 10 seconds.

## Known limitations

- The plugin assumes `rexxlint` supports `--stdin` for both lint and format commands.
- JSON parsing is intentionally tolerant but still depends on line/column data being present.
- Formatting failure UX currently uses IDE notifications; there is no dedicated settings-level diagnostics panel yet.
