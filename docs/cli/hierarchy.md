# CLI: Hierarchy Commands

Hierarchy is maintained through dedicated add/remove subcommands that create **edges**.

| Relationship | Add | Remove |
|--------------|-----|--------|
| Group membership | `add group-member <group> <member>` | `remove group-member <group> <member>` |
| Sub-operation | `add sub-operation <parent> <child>` | `remove sub-operation <parent> <child>` |
| Sub-resource | `add sub-resource <parent> <child>` | `remove sub-resource <parent> <child>` |

## Checks

```text
is-member-of <group> <member>
is-sub-operation <parent> <child>
is-sub-resource <parent> <child>
```

## Listing with flat expansion

```text
get members <group> true
get operations <parent> true
```

Flat mode walks the transitive closure so nested members/ops appear in one list.
