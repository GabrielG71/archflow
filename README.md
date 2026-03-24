# archflow

Interactive command-line utility for compressing and extracting archives on Linux.

---

## Overview

archflow guides the user through compression and extraction operations via an interactive terminal flow. You provide an input path and archflow detects the format, asks for the destination, confirms before overwriting, and delegates the operation to the appropriate system binary.

The project is built with Java 21 and Maven, follows a clean layered architecture, and keeps external dependencies minimal. Archive operations are handled by native Linux tools — archflow is responsible for orchestration, validation, and the interactive flow.

---

## Supported formats

| Format    | Compression | Extraction |
|-----------|-------------|------------|
| `.zip`    | yes         | yes        |
| `.tar.gz` | yes         | yes        |
| `.7z`     | yes         | yes        |
| `.xz`     | no          | yes        |

Format detection is based exclusively on file extension.

---

## MVP limitations

- Linux only
- No support for `.rar`
- No support for `.tar.xz`
- No password or encryption support
- No progress bar
- No multithreading
- `.xz` extraction only — compression outputs a single compressed stream, not an archive; compressing directories requires `.tar.xz`, which is out of scope

---

## Requirements

### System binaries

archflow delegates archive operations to the following tools. Install them before running:

```bash
sudo apt install zip unzip tar xz-utils p7zip-full
```

| Binary  | Package       | Used for                   |
|---------|---------------|----------------------------|
| `zip`   | `zip`         | `.zip` compression         |
| `unzip` | `unzip`       | `.zip` extraction          |
| `tar`   | `tar`         | `.tar.gz` compression and extraction |
| `7z`    | `p7zip-full`  | `.7z` compression and extraction |
| `xz`    | `xz-utils`    | `.xz` extraction           |

### Java

Java 21 or later is required.

---

## Building

```bash
mvn clean package
```

This produces a fat JAR at `target/archflow-0.1.0-SNAPSHOT.jar` with all dependencies bundled.

---

## Running

```bash
java -jar target/archflow-0.1.0-SNAPSHOT.jar
```

archflow starts in interactive mode. No flags or arguments are required.

---

## How it works

When launched, archflow walks the user through the following steps:

1. Asks for the input path and validates that it exists and is readable
2. If the path is a directory or a file with an unrecognized extension, offers compression
3. If the path is a file with a recognized archive extension, proceeds to extraction
4. Asks for the output directory and the final file or directory name
5. Warns and asks for confirmation if the destination already exists
6. Executes the operation and prints the result

All operations are logged to `logs/archflow.log`.

---

## Running tests

```bash
mvn test
```

The test suite covers format detection, path validation, command construction, and handler behavior. Handler tests use real system binaries via `@TempDir` rather than mocks.

---

## Architecture

```
io.archflow
├── cli              # interactive flow, prompts, entry point
├── application      # use cases, orchestration, request/response models
├── domain           # enums, domain models, ArchiveHandler interface, exceptions
├── infrastructure   # archive handlers, ProcessBuilder wrapper, filesystem utilities
└── config           # reserved for future configuration
```

Each archive format has its own isolated handler implementing the `ArchiveHandler` interface. Adding a new format requires only a new handler — no changes to existing code.

---

## Logging

Logs are written to `logs/archflow.log` with daily rotation and a 30-day retention policy. The console receives only `ERROR`-level output to avoid interfering with the interactive CLI. Log entries include operation type, input and output paths, detected format, executed command, and result.

---

## Roadmap

Items planned for after the MVP:

- End-to-end integration tests (compress then extract and verify contents)
- `.tar.xz` support
- `ArchiveFlowRunner` tests with a simulated input stream
- GitHub release with a downloadable fat JAR
- UX improvements in the terminal flow

---

## License

MIT License. See [LICENSE](LICENSE).
