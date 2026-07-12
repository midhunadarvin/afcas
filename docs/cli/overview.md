# CLI Overview

The AFCAS command-line interface is an interactive shell built with **PicoCLI** and **JLine2**.

## Start

```bash
# Fat JAR
java -jar app/build/libs/afcas.jar

# With connection flags
java -jar app/build/libs/afcas.jar -h localhost -p 5432 -U postgres -W

# Native binary
./app/build/native/nativeCompile/afcas -h localhost -p 5432 -U postgres -W
```

If flags are omitted, the shell prompts for hostname, port, username, and password.

## Prompt

Successful connection shows:

```text
Database connected successfully!
afcas>
```

## Top-level structure

```text
afcas
├── connect-db
├── add
│   ├── principal
│   ├── operation
│   ├── resource
│   ├── access-predicate | permission
│   ├── group-member
│   ├── sub-operation
│   └── sub-resource
├── remove | rm
│   ├── principal
│   ├── operation
│   ├── resource
│   ├── access-predicate | permission
│   ├── sub-operation
│   └── sub-resource
├── get
│   ├── principal
│   ├── members
│   ├── operations
│   ├── authorization-digest
│   ├── authorized-operations
│   └── authorized-resources
├── is-authorized
├── is-member-of
├── is-sub-operation
├── is-sub-resource
├── cls | clear
└── help
```

## Global options

| Option | Description |
|--------|-------------|
| `-h`, `--host` | Database host |
| `-p`, `--port` | Database port |
| `-U`, `--username` | Database user |
| `-W`, `--password` | Password prompt |
| `--help` | Help (mixin) |
| `--version` | Version |

## Exit

Press **Ctrl-C** (footer message in CLI).

## Sections

- [Connection](./connection)
- [Principals](./principals)
- [Operations](./operations)
- [Resources](./resources)
- [Permissions](./permissions)
- [Hierarchy](./hierarchy)
- [Queries & Checks](./queries)
- [Cheat Sheet](./cheat-sheet)
