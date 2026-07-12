# CLI: Permissions

Permissions map to **access predicates**. Subcommand name is `access-predicate` with alias **`permission`**.

## Add

```text
add permission <principal> <operation> <resource> <predicateType>
# or
add access-predicate <principal> <operation> <resource> <predicateType>
```

`predicateType`: `Grant` or `Deny`

```text
add permission John Edit Workspace Grant
```

**Important:** the add command throws if type is not `Grant` (`Explicit denials are not supported!`).

## Remove

```text
remove permission <principal> <operation> <resource> <predicateType>
```

```text
remove permission John Edit Workspace Grant
```
