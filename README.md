
## AFCAS

A Java / PostgreSQL port of "A fairly capable authorization sub system"

Based on : https://www.codeproject.com/Articles/30380/A-Fairly-Capable-Authorization-Sub-System-with-Row

### Prerequisite

All the scripts in the `sql` folder must be run in the postgres database where we will be persisting the authorization permissions.

Gradle : https://gradle.org/install/

### Build

``gradle shadowJar``

### Run

The build file will be created at `app/build/libs/afcas.jar`

```java -jar afcas.jar```

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

##### Remove Principal
```
remove principal <name>
```

Eg.
``remove principal John``

<br/>

##### Add Operation 
```
add operation <id> <name>
```

Eg.
``add operation Edit EditOperation``

<br/>

##### Remove Operation
```
remove operation <name>
```

Eg.
``remove operation Edit``

<br/>

##### Add Resource 
```
add resource <id> <name>
```

Eg.
``add resouce workspace Workspaces``

<br/>

##### Remove Resource
```
remove resource <name>
```

Eg.
``remove resource Workspace``

<br/>

##### Check if Authorized
```
is-authorized <principle> <operation> <workspace>
```

Eg.
``is-authorized John Edit Workspace``

<br/>

