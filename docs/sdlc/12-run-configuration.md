# Rexx Run Configuration

## Configure the interpreter
Create a **Rexx** run configuration and set:
- **Interpreter**: executable name or absolute path, for example `rexx`, `regina`, or `ooRexx`
- **Script**: Rexx source file to execute
- **Working directory**: optional process working directory
- **Arguments**: optional command-line arguments passed after the script path

## Run a Rexx file
1. Open **Run | Edit Configurations...**
2. Add a new **Rexx** configuration
3. Fill in interpreter and script path
4. Save and run the configuration

## Supported interpreters
The configuration is interpreter-agnostic and currently targets common Rexx CLIs such as:
- Regina Rexx (`regina`)
- classic `rexx`
- ooRexx (`ooRexx`)

## Command shape
The process is launched in this form:

```text
<interpreter> <scriptPath> [program arguments...]
```

Example:

```text
regina /path/to/example.rexx --verbose sample-input
```
