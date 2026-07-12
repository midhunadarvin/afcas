# Access Predicates (Permissions)

An **access predicate** is the atomic permission rule:

```
(PrincipalId, OperationId, ResourceId, PredicateType)
```

## Predicate types

| Name | Value | Meaning |
|------|------:|---------|
| Grant | 0 | Allow |
| Deny | 1 | Deny (schema-level; CLI add path disallows Deny) |

## Primary key

Composite of all four columns — the same triple may exist with different types if you insert Deny via SQL.

## CLI

Commands use the alias **`permission`** for **`access-predicate`**:

```text
add permission John Edit Workspace Grant
remove permission John Edit Workspace Grant
```

## Decision usage

Only **Grant** is consulted by the standard `IsAuthorized` implementation: existence of any matching Grant after hierarchy expansion returns true.

## Web UI

`/permissions` — form-based add/remove of grants.
