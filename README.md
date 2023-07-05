
## AFCAS

A Java / Postgres port of "A fairly capable authorization sub system"

Based on : https://www.codeproject.com/Articles/30380/A-Fairly-Capable-Authorization-Sub-System-with-Row

### Useage

##### Connect to Database
```
connect-db -h <db-url> -p <db-port> -U <db-user> -W
```

Eg.
``connect-db -h localhost -p 5432 -U postgres -W``

<br/>

##### Add Principal 
```
add principal <name> <principal-type> <display-name> <email> <description> <source>
```

Eg.
``add principal John User John john@gmail.com test TestSource``

<br/>

##### Add Operation 
```
add operation <id> <name>
```

Eg.
``add operation Edit EditOperation``

<br/>

##### Add Resource 
```
add resource <id> <name>
```

Eg.
``add resouce workspace Workspaces``

