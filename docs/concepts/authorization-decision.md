# Authorization Decision

## API

```text
isAuthorized(principalId, operationId, resourceId) → boolean
```

CLI:

```text
is-authorized John Edit Workspace
```

## Algorithm (simplified)

1. **Validate** principal and operation are non-empty (resource may be empty).
2. **Expand principal:** `{ principalId } ∪ ancestors(principalId, Principal)`
3. **Expand operation:** `{ operationId } ∪ ancestors(operationId, Operation)`
4. **Expand resource:** `{ resourceId } ∪ ancestors(resourceId, Resource)` (or `{""}` if null/empty)
5. **Probe grants:** for each combination `(p, o, r)`, if `AccessPredicate` has Grant → return **true**
6. Otherwise **false**

### SQL sketch

```sql
SELECT EXISTS (
  -- expanded principals
  (SELECT "EndVertex" FROM "Edge"
     WHERE "Source" = 'Principal' AND "StartVertex" = :principal
   UNION SELECT :principal)
  CROSS JOIN
  -- expanded operations
  ...
  CROSS JOIN
  -- expanded resources
  ...
  INNER JOIN "AccessPredicate" ap ON ...
  WHERE ap."PredicateType" = 0  -- Grant
);
```

## Related queries

| Method | Purpose |
|--------|---------|
| `getAuthorizationDigest(principal)` | All grants relevant to a principal (effective set) |
| `getAuthorizedOperations(principal, resource)` | Ops allowed on a resource |
| `getAuthorizedResources(principal, operation)` | Resources allowed for an op |
| `isMemberOf(group, member)` | Membership check via edges |
| `isSubOperation` / `isSubResource` | Hierarchy checks |

## Engine modes

Same logical algorithm runs in:

- PostgreSQL function `IsAuthorized` (`sql` mode)
- `AuthorizationEngine` (`java` mode)

Configure with `afcas.engine.mode`.
