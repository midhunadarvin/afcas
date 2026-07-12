# Domain Model Reference

## Enums

```java
enum PrincipalType { User, Group }
enum ResourceAccessPredicateType { Grant, Deny }
enum EdgeSource { Principal, Operation, Resource }
enum EngineMode { SQL, JAVA }
```

## Classes

### Principal

- `id`, `name`, `email`, `displayName`, `description`, `dataSource`
- `principalType`

### Operation

- `id`, `name`, `description`

### Resource

- `id`, `name`

### ResourceAccessPredicate

- `principalId`, `operationId`, `resourceId`
- `accessPredicateType`

### Edge

- `startVertex`, `endVertex`, `hops`, `source`, `delMark`

## Interfaces

See [Java Layer](/architecture/java-layer) for full method lists on:

- `IAuthorizationManager`
- `IAuthorizationProvider`
