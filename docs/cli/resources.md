# CLI: Resources

## Add

```text
add resource <id> <name>
```

```text
add resource Workspace Workspaces
```

> Note: older README text sometimes typo’d this as `add resouce` — the real command is **`add resource`**.

## Remove

```text
remove resource <name>
```

## Sub-resources

```text
add sub-resource <parent-resource> <sub-resource>
remove sub-resource <parent-resource> <sub-resource>
is-sub-resource <resource-name> <sub-resource-name>
```

```text
add sub-resource Workspace WorkspaceA
```
