# AFCAS Architecture Document

## Overview

**AFCAS** (A Fairly Capable Authorization Sub-System) is a role-based access control (RBAC) and permission management system built on PostgreSQL. It provides a flexible authorization framework for managing:

- **Principals** (Users and Groups)
- **Operations** (Actions that can be performed)
- **Resources** (Entities being accessed)
- **Access Predicates** (Grant/Deny permissions)
- **Hierarchical Relationships** (Group membership, operation inheritance, resource hierarchy)

Based on: [A Fairly Capable Authorization Sub-System (CodeProject)](https://www.codeproject.com/Articles/30380/A-Fairly-Capable-Authorization-Sub-System-with-Row)

---

## System Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                        Application Layer                        │
├─────────────────────────┬───────────────────────────────────────┤
│   Web GUI (Spring Boot) │           CLI (PicoCLI)               │
│   - Thymeleaf SSR       │           - Interactive Shell         │
│   - REST-like pages     │           - Scriptable commands       │
└────────────┬────────────┴───────────────────────┬───────────────┘
             │                                    │
             └───────────────┬────────────────────┘
                             │
┌────────────────────────────▼────────────────────────────────────┐
│                    Service Layer (Java)                         │
├─────────────────────────────────────────────────────────────────┤
│  IAuthorizationManager  │  IAuthorizationProvider               │
│  (CRUD operations)      │  (Query/Decision operations)          │
│  - addOrUpdate()        │  - isAuthorized()                     │
│  - remove()             │  - isMemberOf()                       │
│  - getList()            │  - getAuthorizationDigest()           │
└────────────┬────────────┴───────────────────────┬───────────────┘
             │                                    │
             └───────────────┬────────────────────┘
                             │
┌────────────────────────────▼────────────────────────────────────┐
│                 Database Layer (PostgreSQL)                     │
├─────────────────────────────────────────────────────────────────┤
│  Tables: Principal, Operation, Resource, AccessPredicate, Edge  │
│  Procedures: AddOrUpdate*, Remove*, Get*List, Is*, Edge*        │
│  Functions: GetAuthorizationDigest, GetAuthorized*              │
└─────────────────────────────────────────────────────────────────┘
```

---

## Database Schema

### Core Tables

#### 1. Principal
Stores users and groups (both share this table, differentiated by `PrincipalType`).

| Column | Type | Description |
|--------|------|-------------|
| `ObjectId` | UUID | Primary key (auto-generated) |
| `Name` | VARCHAR(256) | Unique identifier (e.g., "john", "admins") |
| `PrincipalType` | SMALLINT | 0=User, 1=Group |
| `DisplayName` | VARCHAR(500) | Human-readable name |
| `Email` | VARCHAR(500) | Contact email |
| `Description` | VARCHAR(500) | Additional details |
| `DataSource` | VARCHAR(100) | Origin/source system |

**Index:** Unique on `Name`

#### 2. Operation
Defines actions that can be performed on resources.

| Column | Type | Description |
|--------|------|-------------|
| `Id` | VARCHAR(100) | Primary key (e.g., "Read", "Write", "Delete") |
| `Name` | VARCHAR(250) | Display name |
| `Description` | VARCHAR(500) | What this operation does |

#### 3. Resource
Defines entities that can be accessed.

| Column | Type | Description |
|--------|------|-------------|
| `Id` | VARCHAR(100) | Primary key (e.g., "Workspace", "Document123") |
| `Name` | VARCHAR(100) | Display name |

#### 4. AccessPredicate
The core permission table - defines Grant/Deny rules.

| Column | Type | Description |
|--------|------|-------------|
| `PrincipalId` | VARCHAR(256) | FK to Principal.Name |
| `OperationId` | VARCHAR(100) | FK to Operation.Id |
| `ResourceId` | VARCHAR(256) | FK to Resource.Id |
| `PredicateType` | SMALLINT | 0=Grant, 1=Deny |

**Primary Key:** Composite of all four columns

#### 5. Edge
Stores hierarchical relationships (transitive closure graph).

| Column | Type | Description |
|--------|------|-------------|
| `StartVertex` | VARCHAR(256) | Child node (e.g., user name, operation id) |
| `EndVertex` | VARCHAR(256) | Parent node (e.g., group name) |
| `Hops` | INTEGER | Distance: 0=direct, 1+=transitive |
| `Source` | VARCHAR(150) | Type: "Principal", "Operation", "Resource" |
| `DelMark` | BOOLEAN | Soft delete marker |

**Indexes:** On (StartVertex, EndVertex), (EndVertex, StartVertex), Hops, Source, DelMark

---

## Domain Model (Java)

```java
// Enums
enum PrincipalType { User, Group }
enum ResourceAccessPredicateType { Grant, Deny }
enum EdgeSource { Principal, Operation, Resource }

// Domain Objects
class Principal {
    String id, name, email, displayName, description, dataSource;
    PrincipalType principalType;
}

class Operation {
    String id, name, description;
}

class Resource {
    String id, name;
}

class ResourceAccessPredicate {
    String principalId, operationId, resourceId;
    ResourceAccessPredicateType accessPredicateType;
}
```

---

## Key Concepts

### 1. Hierarchical Authorization

AFCAS supports **inheritance through hierarchy**:

- **Principals:** A user in a group inherits the group's permissions
- **Operations:** Child operations inherit parent operation permissions
- **Resources:** Child resources inherit parent resource permissions

Example:
```
Group "Admins" (has permission: Grant, "Edit", "Workspace")
  └── User "John" (inherits "Edit" on "Workspace")

Operation "Edit" (has permission)
  └── Operation "Delete" (inherits permission)

Resource "Workspace" (has permission)
  └── Resource "WorkspaceA" (inherits permission)
```

### 2. Edge-Based Transitive Closure

The `Edge` table stores a **transitive closure** of the hierarchy graph:

- `Hops = 0`: Direct parent-child relationship
- `Hops = 1`: One level of transitivity
- `Hops = N`: N levels of transitivity

When adding a relationship via `AddEdgeWithSpaceSavings`:
1. Creates direct edge (Hops=0)
2. Computes and stores all transitive edges (Hops=1+)
3. Prevents circular relationships

This design allows **O(1) authorization checks** by querying for edges at any hop level.

### 3. Authorization Decision Logic

The `IsAuthorized(principalId, operationId, resourceId)` function:

1. Expands the **principal** to include all ancestors (groups)
2. Expands the **operation** to include all parent operations
3. Expands the **resource** to include all parent resources
4. Checks if any combination exists in `AccessPredicate` with `PredicateType = Grant`
5. Returns `TRUE` if found, `FALSE` otherwise

```sql
-- Simplified logic:
SELECT EXISTS (
    -- All principals (self + ancestors via Edge)
    (SELECT EndVertex FROM Edge WHERE Source='Principal' AND StartVertex=principalId
     UNION SELECT principalId)
    CROSS JOIN
    -- All operations (self + ancestors via Edge)
    (SELECT EndVertex FROM Edge WHERE Source='Operation' AND StartVertex=operationId
     UNION SELECT operationId)
    CROSS JOIN
    -- All resources (self + ancestors via Edge)
    (SELECT EndVertex FROM Edge WHERE Source='Resource' AND StartVertex=resourceId
     UNION SELECT resourceId)
    INNER JOIN AccessPredicate ON ...
    WHERE PredicateType = 0  -- Grant
)
```

---

## Stored Procedures Reference

### Principal Operations

| Procedure/Function | Description |
|-------------------|-------------|
| `AddOrUpdatePrincipal(name, type, displayName, email, desc, source)` | Create or update a user/group |
| `RemovePrincipal(name)` | Delete principal (also removes related edges) |
| `GetPrincipalList(type?)` | List all principals (optionally filter by type) |
| `GetMembersList(groupName, isFlat)` | Get members of a group (isFlat=1 for all levels) |

### Operation Operations

| Procedure/Function | Description |
|-------------------|-------------|
| `AddOrUpdateOperation(id, name, desc)` | Create or update an operation |
| `RemoveOperation(id)` | Delete operation (also removes related edges) |
| `GetOperationList()` | List all operations |
| `GetSubOperationsList(opId, isFlat)` | Get sub-operations (isFlat=1 for all levels) |

### Resource Operations

| Procedure/Function | Description |
|-------------------|-------------|
| `AddOrUpdateResource(id, name)` | Create or update a resource |
| `RemoveResource(id)` | Delete resource (also removes related edges) |

### Access Predicate Operations

| Procedure/Function | Description |
|-------------------|-------------|
| `AddAccessPredicate(principal, operation, resource, type)` | Grant or deny permission |
| `RemoveAccessPredicate(principal, operation, resource, type)` | Remove permission |
| `IsAuthorized(principal, operation, resource)` | Check if authorized (boolean) |
| `GetAuthorizationDigest(principal)` | Get all permissions for a principal |
| `GetAuthorizedOperations(principal, resource)` | Get ops principal can do on resource |
| `GetAuthorizedResources(principal, operation)` | Get resources principal can access |

### Edge/Hierarchy Operations

| Procedure/Function | Description |
|-------------------|-------------|
| `AddEdgeWithSpaceSavings(start, end, source)` | Add relationship (auto-computes transitive closure) |
| `RemoveEdgeWithSpaceSavings(start, end, source)` | Remove relationship (updates transitive edges) |
| `EdgeExists(start, end, source)` | Check if relationship exists |
| `RemoveRelatedEdges(node, source)` | Remove all edges involving a node (called on delete) |

---

## Web Application Architecture

### Technology Stack

| Layer | Technology |
|-------|------------|
| **Framework** | Spring Boot 3.1.5 |
| **UI** | Thymeleaf (SSR) + Bootstrap 5 |
| **Database** | PostgreSQL (JDBC via existing `DatabaseHelper`) |
| **Build** | Gradle 8.5 |

### Project Structure

```
com.afcas/
├── web/                           # Spring Boot Web Layer
│   ├── AfcasWebApplication.java   # Main entry point
│   ├── config/
│   │   └── DatabaseConfig.java    # Initializes DB connection
│   ├── controller/
│   │   ├── HomeController.java    # Dashboard
│   │   ├── PrincipalsController.java
│   │   ├── OperationsController.java
│   │   ├── ResourcesController.java
│   │   ├── PermissionsController.java
│   │   └── AuthorizationController.java
│   └── service/
│       └── AuthorizationService.java  # Wraps existing managers
├── impl/
│   ├── AuthorizationManager.java  # CRUD operations
│   └── AuthorizationProvider.java # Query/decision operations
├── objects/                       # Domain models
│   ├── Principal.java
│   ├── Operation.java
│   ├── Resource.java
│   └── ResourceAccessPredicate.java
└── utils/
    └── DatabaseHelper.java        # JDBC wrapper for stored procs
```

### Controllers Mapping

| Path | Controller | Description |
|------|------------|-------------|
| `/` | HomeController | Dashboard |
| `/principals` | PrincipalsController | List/Add/Remove principals, manage group members |
| `/operations` | OperationsController | List/Add/Remove operations |
| `/resources` | ResourcesController | Info page (resources via permissions) |
| `/permissions` | PermissionsController | Add/Remove access predicates (Grant/Deny) |
| `/authorization` | AuthorizationController | Check authorization, get digest, authorized ops/resources |

---

## Usage Examples

### Add a User
```sql
CALL "AddOrUpdatePrincipal"('john', 0, 'John Doe', 'john@example.com', 'Developer', 'manual');
```

### Create a Group and Add Member
```sql
CALL "AddOrUpdatePrincipal"('admins', 1, 'Administrators', '', 'Admin group', 'manual');
CALL "AddEdgeWithSpaceSavings"('john', 'admins', 'Principal');  -- John is in Admins
```

### Define an Operation
```sql
CALL "AddOrUpdateOperation"('Edit', 'Edit', 'Edit resources');
```

### Create a Resource
```sql
CALL "AddOrUpdateResource"('Workspace', 'Workspace');
```

### Grant Permission
```sql
CALL "AddAccessPredicate"('admins', 'Edit', 'Workspace', 0);  -- 0=Grant
```

### Check Authorization
```sql
SELECT "IsAuthorized"('john', 'Edit', 'Workspace');  -- TRUE (inherited from admins)
```

---

## Design Principles

1. **Performance:** Transitive closure pre-computed in `Edge` table for O(1) auth checks
2. **Flexibility:** Same schema supports users, groups, operations, and resources
3. **Safety:** Circular relationship prevention in edge procedures
4. **Simplicity:** Stored procedures encapsulate complex logic
5. **Extensibility:** New entity types can use the same Edge mechanism

---

## Running the Application

### Prerequisites
- PostgreSQL 14+ with database created
- SQL scripts executed (see `sql/` folder)
- Java 21 (or 17+)

### Start the Web UI
```bash
./gradlew bootRun
# Open http://localhost:8080

./gradlew bootJar
java -jar app/build/libs/afcas-web-1.0-SNAPSHOT.jar
```

### Start the CLI
```bash
./gradlew shadowJar
java -jar app/build/libs/afcas.jar -h localhost -p 5432 -U postgres -W
# or
./gradlew :app:run --args='-h localhost -p 5432 -U postgres'
```

### Database Configuration
Defaults in `application.properties` (web / Spring):
```properties
afcas.database.host=localhost
afcas.database.port=5432
afcas.database.username=postgres
afcas.database.password=123456
```

CLI connection uses PicoCLI flags (`-h`, `-p`, `-U`, `-W`) instead.

Override web config:
```bash
java -jar app/build/libs/afcas-web-1.0-SNAPSHOT.jar --afcas.database.password=yourpass
```

---

## License

See [LICENSE](LICENSE) file.

---

*Generated from analysis of SQL procedures and Java source code.*
