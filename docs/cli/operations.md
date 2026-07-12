# CLI: Operations

## Add

```text
add operation <id> [<name>] [<description>]
```

```text
add operation Edit EditOperation
```

## Remove

```text
remove operation <name>
```

```text
remove operation Edit
```

## List / hierarchy queries

```text
get operations [<parent-operation>] [<is-flat>]
```

```text
get operations
get operations Edit
get operations Edit true
```

## Sub-operations

```text
add sub-operation <parent-operation> <sub-operation>
remove sub-operation <parent-operation> <sub-operation>
is-sub-operation <operation-name> <sub-operation-name>
```

```text
add sub-operation ViewEdit View
is-sub-operation ViewEdit View
```
