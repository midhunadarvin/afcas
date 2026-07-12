# Authorization Model

AFCAS implements hierarchical RBAC: a **principal** may perform an **operation** on a **resource** if a matching **Grant** exists after expanding all three dimensions through hierarchy.

## The four building blocks

```
Principal ──performs──► Operation ──on──► Resource
     │                      │                 │
     └──── group graph ─────┴── op graph ─────┴── resource graph
```

1. **Principal** — who is acting (`User` or `Group`)
2. **Operation** — what action is requested
3. **Resource** — what object is targeted
4. **AccessPredicate** — stored rule: `(principal, operation, resource, Grant|Deny)`

## Inheritance

Permissions are not limited to the exact triple stored in the database. When checking authorization, AFCAS expands:

| Dimension | Expansion |
|-----------|-----------|
| Principal | Self + all ancestor groups (via `Edge` source `Principal`) |
| Operation | Self + all parent operations (source `Operation`) |
| Resource | Self + all parent resources (source `Resource`) |

If **any** combination of expanded principals × operations × resources matches a **Grant** row, the check succeeds.

### Example

```
Group "Admins"  has Grant Edit on Workspace
User "John"     is member of Admins
```

`IsAuthorized(John, Edit, Workspace)` → **true** (John expands to include Admins).

Add hierarchy:

```
Operation "Manage" parent of "Edit"
Resource "Workspace" parent of "WorkspaceA"
```

Then grants on parent operation/resource also cover children, depending on edge direction and how ancestors are defined (see [Hierarchy & Edges](./hierarchy-and-edges)).

## Deny support

The schema includes `PredicateType` **Grant (0)** and **Deny (1)**. The CLI currently rejects explicit denials when adding permissions (`Explicit denials are not supported!`). Treat Deny as reserved / future unless you use SQL directly and extend decision logic.

## Interfaces for clients

- **`IAuthorizationManager`** — mutate principals, ops, resources, predicates, hierarchy; list entities
- **`IAuthorizationProvider`** — `isAuthorized`, membership/sub-relation checks, digests, authorized ops/resources

Application code should prefer these interfaces over calling SQL or repositories directly.

## Offline / bulk use

`getAuthorizationDigest(principal)` returns the effective permission set for a principal (useful for caching or offline checks).  
`getAuthorizedOperations` / `getAuthorizedResources` support UI browsing of allowed actions and targets.
