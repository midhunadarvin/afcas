# CLI Connection

## On startup

```bash
java -jar afcas.jar -h <db-hostname> -p <db-port> -U <username> -W
```

Interactive fallback reads each missing value from the console.

## Reconnect mid-session

```text
connect-db -h <db-url> -p <db-port> -U <db-user> -W
```

Example:

```text
connect-db -h localhost -p 5432 -U postgres -W
```

## Implementation notes

- Connection is managed by `DatabaseHelper.init(...)`
- `ConnectDB` command re-initializes the shared helper
- Ensure SQL schema is already applied (or use the web app auto-init path first)
