# Principals

A **principal** is an identity that can be granted permissions. Both users and groups live in the same `Principal` table.

## Types

| Type | Enum | Typical use |
|------|------|-------------|
| User | `PrincipalType.User` | Individual account |
| Group | `PrincipalType.Group` | Role-like container of members |

## Fields

| Field | Description |
|-------|-------------|
| `Name` | Unique logical id (used as principal id in predicates and edges) |
| `PrincipalType` | User or Group |
| `DisplayName` | Human-readable label |
| `Email` | Contact |
| `Description` | Free text |
| `DataSource` | Origin system (e.g. `manual`, `ldap`) |
| `ObjectId` | UUID primary key in the database |

## Group membership

Membership is **not** a separate table. Adding a member creates an **Edge** with `Source = Principal`:

- Start = member
- End = group

Nested groups are supported; transitive edges make multi-level membership efficient.

## CLI examples

```text
add principal John User John john@gmail.com test TestSource
add principal Admin Group
add group-member Admin John
remove group-member Admin John
remove principal John
get principal
get members Admin true
is-member-of Admin John
```

## Web UI

Manage principals at `/principals` (list, create, delete, manage members).
