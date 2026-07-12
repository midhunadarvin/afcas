# CLI: Queries & Checks

## Authorization check

```text
is-authorized <principal> <operation> <resource>
```

```text
is-authorized John Edit Workspace
```

## Authorization digest

All relevant grants for a principal:

```text
get authorization-digest <principal-name>
```

```text
get authorization-digest John
```

## Authorized operations

```text
get authorized-operations <principal-name> <resource-name>
```

```text
get authorized-operations John Workspace
```

## Authorized resources

```text
get authorized-resources <principal-name> <operation-name>
```

```text
get authorized-resources John Edit
```

## Entity lists

```text
get principal
get operations
get members <group> [flat]
```
