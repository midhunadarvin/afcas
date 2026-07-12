# CLI Cheat Sheet

```text
# Connection
./afcas -h localhost -p 5432 -U postgres -W
connect-db -h localhost -p 5432 -U postgres -W

# Principals
add principal John User John john@gmail.com test TestSource
add principal Admin Group
remove principal John
get principal
add group-member Admin John
remove group-member Admin John
get members Admin true
is-member-of Admin John

# Operations
add operation Edit EditOperation
remove operation Edit
get operations
get operations Edit true
add sub-operation ViewEdit View
remove sub-operation ViewEdit View
is-sub-operation ViewEdit View

# Resources
add resource Workspace Workspaces
remove resource Workspace
add sub-resource Workspace WorkspaceA
remove sub-resource Workspace WorkspaceA
is-sub-resource Workspace WorkspaceA

# Permissions
add permission John Edit Workspace Grant
remove permission John Edit Workspace Grant

# Authorization
is-authorized John Edit Workspace
get authorization-digest John
get authorized-operations John Workspace
get authorized-resources John Edit

# Shell
cls
help
```
