# Resources

A **resource** is an object or scope being protected.

## Fields

| Field | Description |
|-------|-------------|
| `Id` | Primary key (used in predicates and edges) |
| `Name` | Display name |

## Hierarchy

Resources can nest (folder → document, org → workspace) via edges with `Source = Resource`.

```text
add resource Workspace Workspaces
add sub-resource Workspace WorkspaceA
remove sub-resource Workspace WorkspaceA
is-sub-resource Workspace WorkspaceA
remove resource Workspace
```

## Authorization without a resource

`isAuthorized` allows empty/null resource id in the Java engine (treated as `""`). Prefer always specifying a resource in production designs.

## Web UI

`/resources` — manage resource entities.
