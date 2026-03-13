# CLAUDE.md

## Project Overview

This project is called **archflow**.

It is a **professional interactive CLI archive utility** written in **Java 21 + Maven**, focused initially on **Linux**.

The goal is to provide a clean, reliable, open source command-line tool to **compress and extract files/directories** using a guided interactive terminal flow.

This is not a GUI project.
This is not a web project.
This is not a monolithic all-in-one archiver.
It should stay focused, minimal, well-architected, and production-minded.

---

## Core Product Goals

The application must:

- run in the Linux terminal
- open in interactive CLI mode
- ask the user for an input path
- validate whether the path exists
- detect archive format **only by file extension**
- print the detected extension when compatible
- print `extensão não compatível` when unsupported
- ask the user what operation should be performed
- ask where the output should be saved
- ask for the final output name
- warn before overwriting existing files
- write logs to a dedicated log file
- keep the architecture simple, modular, and extensible

---

## MVP Scope

### Supported extraction formats
- `.zip`
- `.tar.gz`
- `.7z`
- `.xz`

### Supported compression formats
- `.zip`
- `.tar.gz`
- `.7z`

### Not included in MVP
- `.rar`
- Windows support
- magic-byte content detection
- passwords/encryption
- progress bars
- advanced TUI
- multithreading
- `.tar.xz`
- native Java compression engines for every format

---

## Platform Strategy

### Initial platform target
- Linux only

### Technical approach
Use a **hybrid strategy**:

- Java handles:
  - CLI flow
  - validation
  - orchestration
  - business rules
  - logs
  - error handling
- System binaries handle archive operations:
  - `zip`
  - `unzip`
  - `tar`
  - `7z` or `7zz`
  - `xz`

Use `ProcessBuilder` to execute commands.

Do not overengineer native Java archive support in the MVP if external Linux tools provide a cleaner and more reliable path.

---

## Architecture Requirements

The codebase must be clearly separated from the beginning.

Use this package structure:

- `cli`
- `application`
- `domain`
- `infrastructure`
- `config`

### Responsibilities

#### `cli`
Interactive user prompts, terminal messages, menu flow, confirmations.

#### `application`
Use cases, orchestration, request/response DTOs, operation coordination.

#### `domain`
Enums, contracts, core business rules, exceptions, archive abstractions.

#### `infrastructure`
Process execution, filesystem operations, archive handlers, logging setup.

---

## Design Principles

- Keep the solution simple, direct, and well-finished
- Avoid unnecessary abstractions unless they clearly improve maintainability
- Prefer explicitness over cleverness
- Prioritize good naming
- Keep classes focused and cohesive
- Make future contributions easy
- Leave room for future pull requests and new formats
- Do not turn this into enterprise boilerplate

---

## Required Domain Concepts

Create and use domain models such as:

- `ArchiveFormat`
- `InputType`
- `OperationType`
- `OverwriteDecision`
- `CompressionRequest`
- `ExtractionRequest`
- `OperationResult`

Create an abstraction such as:

- `ArchiveHandler`

Each supported format should have its own handler implementation.

Examples:
- `ZipHandler`
- `TarGzHandler`
- `SevenZipHandler`
- `XzHandler`

---

## Behavior Rules

### Path validation
The application must validate:
- path existence
- whether it is a file or directory
- read permissions where needed
- write viability for output destination

### Format detection
Format detection is based **only on extension**.

Supported extensions:
- `.zip`
- `.tar.gz`
- `.7z`
- `.xz`

If unsupported:
- print exactly: `extensão não compatível`

### Operation behavior
- archive file -> extraction flow
- regular file -> compression flow
- directory -> compression flow

### Output naming
Always ask the user for the final output name.

### Overwrite policy
If destination already exists:
- warn clearly
- ask for confirmation
- overwrite only after explicit confirmation

### Logging
Write logs to a dedicated file.
Log:
- operation type
- input path
- output path
- detected format
- selected handler
- executed system command
- success/failure
- error details

---

## Security and Safety Expectations

Even though the MVP relies on system binaries, the application should still evaluate context before destructive actions.

At minimum:
- never overwrite silently
- validate destination paths
- avoid unsafe assumptions
- centralize overwrite confirmation logic
- keep room for future path safety analysis improvements

Do not add fake security features.
Do not claim to protect against things that are not actually implemented.

---

## Tooling

### Language and build
- Java 21
- Maven

### Recommended libraries
- Picocli
- SLF4J
- Logback

Keep dependencies minimal.

---

## Coding Style

- Use English for code, class names, package names, methods, enums, and comments
- User-facing terminal messages may be in Portuguese
- Avoid unnecessary comments
- Add comments only when they clarify non-obvious reasoning
- Prefer small methods with clear names
- Avoid giant god classes
- Avoid premature generics-heavy abstractions

---

## Testing Expectations

At minimum, add tests for:
- extension detection
- path validation logic
- command construction
- overwrite confirmation flow where possible

Focus on meaningful unit tests before trying heavy integration tests.

---

## README Expectations

The repository README should clearly explain:

- what archflow is
- current supported formats
- MVP limitations
- Linux-only scope
- required external tools, if used
- how to build
- how to run
- future roadmap

Do not oversell the project.
Be precise and honest.

---

## What Claude Should Optimize For

When generating code or proposing changes, optimize for:

1. correctness
2. clarity
3. maintainability
4. Linux CLI practicality
5. future extensibility without bloating the MVP

Do not add features outside scope unless explicitly requested.
Do not silently change core architectural decisions.
Do not introduce frameworks that are unnecessary for a CLI utility.