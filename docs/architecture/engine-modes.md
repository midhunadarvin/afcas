# Engine Modes (SQL vs Java)

AFCAS can evaluate authorization either in PostgreSQL or in the JVM.

## Configuration

```properties
# application.properties
afcas.engine.mode=sql   # or java
```

`EngineConfiguration` reads the property and exposes the appropriate `IAuthorizationProvider` as `@Primary`.

```java
EngineMode mode = EngineMode.valueOf(engineModeString.toUpperCase());
switch (mode) {
  case JAVA: return javaEngine;
  case SQL:
  default:   return sqlProvider;
}
```

## Comparison

| Aspect | `sql` | `java` |
|--------|-------|--------|
| Logic location | PL/pgSQL procedures/functions | `AuthorizationEngine`, `EdgeManager` |
| DB requirement | Procedures must be installed | Tables still required; procedures optional for decisions |
| Portability | PostgreSQL-specific | Easier multi-DB / in-memory testing |
| Debugging | SQL / DB tools | Java debugger |
| CLI default path | Historically procedure-based via managers | Configurable for provider-backed paths |

## When to use which

- **`sql`** — production parity with original design; mature procedures; default.
- **`java`** — development, unit tests, and portable application-layer authorization decisions.

## Related types

- `com.afcas.engine.EngineMode` — `SQL`, `JAVA`
- `com.afcas.engine.AuthorizationEngine`
- `com.afcas.engine.EdgeManager`
