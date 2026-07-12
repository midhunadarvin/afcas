# CLI: Principals

## Add principal

```text
add principal <name> <principal-type> [<display-name>] [<email>] [<description>] [<source>]
```

- `principal-type`: `User` or `Group` (enum names, case-sensitive as `PrincipalType.valueOf`)

Examples:

```text
add principal John User John john@gmail.com test TestSource
add principal Admin Group
```

## Remove principal

```text
remove principal <name>
```

```text
remove principal John
remove principal Admin
```

## List principals

```text
get principal
```

## Group members

```text
add group-member <group-name> <member-name>
remove group-member <group-name> <member-name>
get members <group-name> [<is-flat>]
is-member-of <group-name> <member-name>
```

Examples:

```text
add group-member Admin John
get members Admin true
is-member-of Admin Bob
```

`is-flat` / flat list: when true, includes nested membership via transitive edges.
