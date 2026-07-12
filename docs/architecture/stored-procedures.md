# Stored Procedures Reference

Procedures and functions are created by scripts `sql/1-*.sql` … `sql/7-*.sql`.

## Principals

| Name | Description |
|------|-------------|
| `AddOrUpdatePrincipal` | Create or update user/group |
| `RemovePrincipal` | Delete principal + related edges |
| `GetPrincipalList` | List principals (optional type filter) |
| `GetMembersList` | Direct or flat member list |

## Operations

| Name | Description |
|------|-------------|
| `AddOrUpdateOperation` | Create/update operation |
| `RemoveOperation` | Delete + edge cleanup |
| `GetOperationList` | List operations |
| `GetSubOperationsList` | Sub-ops, optional flat |

## Resources

| Name | Description |
|------|-------------|
| `AddOrUpdateResource` | Create/update resource |
| `RemoveResource` | Delete + edge cleanup |

## Access predicates

| Name | Description |
|------|-------------|
| `AddAccessPredicate` | Grant/Deny insert |
| `RemoveAccessPredicate` | Remove rule |
| `IsAuthorized` | Boolean authorization |
| `GetAuthorizationDigest` | Effective grants for principal |
| `GetAuthorizedOperations` | Ops for principal+resource |
| `GetAuthorizedResources` | Resources for principal+op |

## Edges

| Name | Description |
|------|-------------|
| `AddEdgeWithSpaceSavings` | Add relation + transitive maintenance |
| `RemoveEdgeWithSpaceSavings` | Remove relation + recompute |
| `EdgeExists` | Existence check |
| `RemoveRelatedEdges` | Cleanup when entity deleted |

## Direct SQL examples

```sql
CALL "AddOrUpdatePrincipal"('john', 0, 'John Doe', 'john@example.com', 'Developer', 'manual');
CALL "AddOrUpdatePrincipal"('admins', 1, 'Administrators', '', 'Admin group', 'manual');
CALL "AddEdgeWithSpaceSavings"('john', 'admins', 'Principal');
CALL "AddOrUpdateOperation"('Edit', 'Edit', 'Edit resources');
CALL "AddOrUpdateResource"('Workspace', 'Workspace');
CALL "AddAccessPredicate"('admins', 'Edit', 'Workspace', 0);
SELECT "IsAuthorized"('john', 'Edit', 'Workspace');
```
