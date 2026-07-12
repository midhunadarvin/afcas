# Operations

An **operation** is an action that may be authorized (for example `Read`, `Edit`, `Delete`).

## Fields

| Field | Description |
|-------|-------------|
| `Id` | Primary key string (used in predicates and edges) |
| `Name` | Display name |
| `Description` | Optional explanation |

## Hierarchy

Operations form a tree/DAG via edges (`Source = Operation`). Parent operations can imply broader capability so that a grant on a parent covers sub-operations in the decision algorithm (see authorization expansion).

```text
add operation Edit EditOperation
add operation ViewEdit ViewEdit
add sub-operation ViewEdit View
remove sub-operation ViewEdit View
get operations
get operations Edit true
is-sub-operation ViewEdit View
remove operation Edit
```

## Web UI

`/operations` — list, create, delete operations.
