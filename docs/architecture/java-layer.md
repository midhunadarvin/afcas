# Java Layer

## Interfaces

### `IAuthorizationManager`

Mutations and listings:

- Principals: `addOrUpdate`, `removePrincipal`, `getPrincipalList`, members lists
- Operations / resources: CRUD
- Access predicates: add/remove
- Hierarchy: `addGroupMember`, `addSubOperation`, `addSubResource` (+ removes)

### `IAuthorizationProvider`

Decisions and read models:

- `isAuthorized`
- `isMemberOf`, `isSubOperation`, `isSubResource`
- `getAuthorizationDigest`
- `getAuthorizedOperations`, `getAuthorizedResources`

## Implementations

| Class | Role |
|-------|------|
| `AuthorizationManager` | CLI/web CRUD via stored procedures |
| `AuthorizationProvider` | SQL-mode provider |
| `AuthorizationEngine` | Java-mode provider (`@Service`) |
| `EdgeManager` | Graph add/remove/ancestors in Java |
| `JdbcEdgeRepository` / `JdbcAccessPredicateRepository` | JDBC access for java mode |

## Database helper

`DatabaseHelper` holds connection state used by the CLI and initialized from:

- Interactive / CLI flags
- Web `DatabaseConfig` (`@PostConstruct` + `DataSource` bean)

## Web layer

```
controller/*  →  AuthorizationService  →  managers / providers
```

Controllers:

| Path | Controller |
|------|------------|
| `/` | `HomeController` |
| `/principals` | `PrincipalsController` |
| `/operations` | `OperationsController` |
| `/resources` | `ResourcesController` |
| `/permissions` | `PermissionsController` |
| `/authorization` | `AuthorizationController` |

## Factory

CLI commands use `AuthorizationManagerFactory.getInstance()` rather than Spring injection, so the CLI can run without a full Spring context when packaged as a pure PicoCLI app.
