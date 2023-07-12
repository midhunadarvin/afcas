
## AFCAS

A Java / PostgreSQL port of "A fairly capable authorization sub system"

Based on : https://www.codeproject.com/Articles/30380/A-Fairly-Capable-Authorization-Sub-System-with-Row

### Prerequisite

All the scripts in the `sql` folder must be run in the postgres database where we will be persisting the authorization permissions.

Gradle : https://gradle.org/install/

GraalVM : https://www.graalvm.org/downloads/#

### Build

To build a JAR file with all the dependencies :

``gradle shadowJar``

To build a native executable with all the dependencies :

``gradle nativeCompile``

When adding new dependencies, if they are using reflection, we need to generate a `reflect-config.json` file so that graalvm can compile the classes ahead of time and package them in the executable.

Refer :
https://graalvm.github.io/native-build-tools/latest/gradle-plugin.html#agent-support

### Run

The JAR build file will be created at `app/build/libs/afcas.jar`

```java -jar afcas.jar```

The native build executable will be created at `app/build/native/nativeCompile/afcas`

```./afcas```

### Useage

##### Connect to Database

The program will ask you for database credentials when it is executed, else you can pass the parameters :

`./afcas -h <db-hostname> -p <db-port> -U <username>`


If you need to connect to different database.

```
connect-db -h <db-url> -p <db-port> -U <db-user> -W
```

Eg.
``connect-db -h localhost -p 5432 -U postgres -W``

<br/>

##### Add Principal 
```
add principal <name> <principal-type> [<display-name>] [<email>] [<description>] [<source>]
```

Eg.

``add principal John User John john@gmail.com test TestSource``

``add principal Admin Group ``
<br/>

##### Remove Principal
```
remove principal <name>
```

Eg.

``remove principal John``

``remove principal Admin ``

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

##### Add Permission / Access Predicate
```
add permission <principle> <operation> <workspace> <predicateType>
```

Eg.
``add permission John Edit Workspace Grant``

<br/>

##### Remove permission / Access Predicate
```
remove permission <principle> <operation> <workspace> <predicateType>
```

Eg.
``remove permission John Edit Workspace Grant``

<br/>

##### Check if Authorized
```
is-authorized <principle> <operation> <workspace>
```

Eg.
``is-authorized John Edit Workspace``

<br/>

##### Add Group Member
```
add group-member <group-name> <member-name>
```

Eg.
``add group-member Admin John``

<br/>

##### Remove Group Member
```
remove group-member <group-name> <member-name>
```

Eg.
``remove group-member Admin John``

<br/>

##### Add Sub Operation
```
add sub-operation <parent-operation> <sub-operation>
```

Eg.
``add sub-operation ViewEdit View``

<br/>

##### Remove Sub Operation
```
remove sub-operation <parent-operation> <sub-operation>
```

Eg.
``remove sub-operation ViewEdit View``

<br/>

##### Add Sub Resource
```
add sub-resource <parent-resource> <sub-resource>
```

Eg.
``add sub-resource Workspace WorkspaceA``

<br/>

##### Remove Sub Resource
```
remove sub-resource <parent-resource> <sub-resource>
```

Eg.
``remove sub-resource Workspace WorkspaceA``

<br/>

##### List Principal
```
get principal
```

<br/>

##### List Members
```
get members <group-name> [<is-flat>]
```

Eg.
``get members Admins true``

<br/>

##### Is Member
```
is-member-of <group-name> <member-name>
```

Eg.
``is-member-of Admins Bob``

<br/>

##### List Operations
```
get operations [<parent-operation>] [<is-flat>]
```

Eg.

``get operations``    
``get operations Edit``  
``get operations Edit true``

<br/>

##### Is Sub Operation
```
is-sub-operation <operation-name> <sub-operation-name>
```

Eg.
``is-sub-operation ViewEdit View``

<br/>

##### Get Authorization Digest
```
get authorization-digest <principal-name>
```

Eg.
``get authorization-digest John``

<br/>

##### Get Authorized Operations
```
get authorized-operations <principal-name> <resource-name>
```

Eg.
``get authorized-operations John Workspace``

<br/>

##### Get Authorized Resources
```
get authorized-resources <principal-name> <operation-name>
```

Eg.
``get authorized-resources John Edit``

<br/>



