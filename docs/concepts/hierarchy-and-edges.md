# Hierarchy & Edges

The **Edge** table is the heart of AFCAS performance. It stores a **transitive closure** of hierarchical relationships for three parallel graphs: Principal, Operation, and Resource.

## Edge columns

| Column | Description |
|--------|-------------|
| `StartVertex` | Child / member side |
| `EndVertex` | Parent / group side |
| `Hops` | `0` = direct edge; `1+` = transitive shortcut |
| `Source` | `Principal`, `Operation`, or `Resource` |
| `DelMark` | Soft-delete marker used during removals |

## Mental model

When you add “John is a member of Admins”:

1. Insert direct edge: `John → Admins` with `Hops = 0`
2. Compute all transitive edges (e.g. if Admins ∈ SuperUsers, add `John → SuperUsers` with higher hops)
3. Reject cycles (cannot add B→A if A→B exists)

Stored procedure: **`AddEdgeWithSpaceSavings`**. Removal: **`RemoveEdgeWithSpaceSavings`**.

## Why pre-compute?

Authorization expands a node to “self + all ancestors” with simple set queries against `Edge`, rather than recursive CTEs on every check. That yields effectively **O(1)**-style checks relative to hierarchy depth (subject to index use and set size).

## Edge sources

| Source | Meaning of edge |
|--------|-----------------|
| `Principal` | Member → Group |
| `Operation` | Sub-operation → Parent operation |
| `Resource` | Sub-resource → Parent resource |

## Space savings algorithm (summary)

`AddEdgeWithSpaceSavings` avoids storing redundant dense closure where possible while still keeping enough edges for ancestor lookups — see SQL in `sql/2-add-edge-procedure.postgres.sql` and the Java port in `EdgeManager`.

## Java engine

In `afcas.engine.mode=java`, `EdgeManager` replicates add/remove and ancestor queries via `EdgeRepository` / `JdbcEdgeRepository`.
