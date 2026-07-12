# Database Schema

Defined in `sql/0-init-tables.postgres.sql`.

## Principal

| Column | Type | Notes |
|--------|------|-------|
| `ObjectId` | UUID | PK |
| `PrincipalType` | SMALLINT | 0=User, 1=Group |
| `Name` | VARCHAR(256) | Unique (`IX_Principal_1`) |
| `Email` | VARCHAR(500) | |
| `DisplayName` | VARCHAR(500) | |
| `Description` | VARCHAR(500) | |
| `DataSource` | VARCHAR(100) | |

## Operation

| Column | Type | Notes |
|--------|------|-------|
| `Id` | VARCHAR(100) | PK |
| `Name` | VARCHAR(250) | |
| `Description` | VARCHAR(500) | |

## Resource

| Column | Type | Notes |
|--------|------|-------|
| `Id` | VARCHAR(100) | PK |
| `Name` | VARCHAR(100) | Nullable |

## AccessPredicate

| Column | Type | Notes |
|--------|------|-------|
| `PrincipalId` | VARCHAR(256) | Part of composite PK |
| `OperationId` | VARCHAR(100) | |
| `ResourceId` | VARCHAR(256) | |
| `PredicateType` | SMALLINT | 0=Grant, 1=Deny |

## Edge

| Column | Type | Notes |
|--------|------|-------|
| `StartVertex` | VARCHAR(256) | |
| `EndVertex` | VARCHAR(256) | |
| `Hops` | INTEGER | 0 direct, ≥1 transitive |
| `Source` | VARCHAR(150) | Principal / Operation / Resource |
| `DelMark` | BOOLEAN | Soft delete |

Indexes: `(StartVertex, EndVertex)`, `(EndVertex, StartVertex)`, `Hops`, `Source`, `DelMark`.

## ER sketch

```
Principal ──┐
Operation ──┼──► AccessPredicate
Resource  ──┘

Principal / Operation / Resource
        └── names appear as vertices in Edge (per Source)
```

There are no formal SQL FKs from AccessPredicate to the entity tables in the base script; consistency is maintained by application/procedure logic.
