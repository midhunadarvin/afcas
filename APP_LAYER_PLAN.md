# Plan: Replicating Database Procedure Logic in the Application Layer

## Executive Summary

This document outlines a plan to replicate the authorization logic currently embedded in PostgreSQL stored procedures into the Java application layer. This enables database portability, improved testability, and reduced coupling to PostgreSQL-specific features.

---

## 1. Current State Analysis

### 1.1 What the Java Layer Currently Does

```
┌─────────────────────────────────────────────────────────────┐
│                     CURRENT ARCHITECTURE                    │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  Java (AuthorizationManager/AuthorizationProvider)          │
│      │                                                      │
│      ▼                                                      │
│  DatabaseHelper.executeStoredProcedure()                    │
│      │                                                      │
│      ▼                                                      │
│  PostgreSQL Stored Procedures:                              │
│  • AddOrUpdatePrincipal()                                   │
│  • AddEdgeWithSpaceSavings()  ← Complex transitive closure  │
│  • IsAuthorized()             ← Core auth decision logic    │
│  • GetAuthorizationDigest()                                 │
│  • etc.                                                     │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

**Current flow:**
- Java code builds parameter arrays
- Calls `DatabaseHelper.executeStoredProcedure("call \"AddOrUpdatePrincipal\"(?,?,?,?,?,?)", params)`
- PostgreSQL does all the work
- Java just returns results

### 1.2 Logic Currently in Stored Procedures

| Category | Procedure | Complexity | Lines of SQL |
|----------|-----------|------------|--------------|
| **CRUD** | AddOrUpdatePrincipal | Simple | ~70 |
| **CRUD** | RemovePrincipal | Simple | ~5 + edge cleanup |
| **CRUD** | AddOrUpdateOperation | Simple | ~20 |
| **CRUD** | AddOrUpdateResource | Simple | ~20 |
| **CRUD** | AddAccessPredicate | Trivial | ~15 |
| **CRUD** | RemoveAccessPredicate | Trivial | ~5 |
| **Edge** | AddEdgeWithSpaceSavings | **Complex** | ~100+ |
| **Edge** | RemoveEdgeWithSpaceSavings | **Complex** | ~150+ |
| **Edge** | EdgeExists | Trivial | ~10 |
| **Auth** | IsAuthorized | **Complex** | ~40 |
| **Auth** | GetAuthorizationDigest | Medium | ~20 |
| **Auth** | GetAuthorizedOperations | Medium | ~15 |
| **Auth** | GetAuthorizedResources | Medium | ~15 |
| **Query** | GetPrincipalList | Simple | ~20 |
| **Query** | GetMembersList | Medium | ~25 |

**Total: ~600+ lines of SQL procedure logic**

---

## 2. Goals of Application-Layer Replication

| Goal | Benefit |
|------|---------|
| **Portability** | Run on any database (MySQL, SQL Server, H2, in-memory for tests) |
| **Testability** | Unit test auth logic without a real database |
| **Simplified Dev** | No need to manage SQL scripts for development |
| **Debugging** | Step through logic in Java debugger |
| **Cloud Flexibility** | Works with serverless databases, Aurora, etc. |
| **Single Codebase** | All logic in one language |

---

## 3. Proposed Architecture

### 3.1 Layered Design

```
┌──────────────────────────────────────────────────────────────────┐
│                        APPLICATION LAYER                         │
├──────────────────────────────────────────────────────────────────┤
│                                                                  │
│  ┌────────────────────────────────────────────────────────────┐ │
│  │              Service Layer (Existing)                       │ │
│  │  AuthorizationService.java                                  │ │
│  └──────────────────────────────┬─────────────────────────────┘ │
│                                 │                                │
│  ┌──────────────────────────────▼─────────────────────────────┐ │
│  │           Domain Logic Layer (NEW)                          │ │
│  │  ┌────────────────────────────────────────────────────────┐ │ │
│  │  │ EdgeManager (transitive closure algorithm)             │ │ │
│  │  │  - addEdge(start, end, source)                         │ │ │
│  │  │  - removeEdge(start, end, source)                      │ │ │
│  │  │  - getAncestors(node, source) → Set<String>            │ │ │
│  │  │  - getDescendants(node, source) → Set<String>          │ │ │
│  │  └────────────────────────────────────────────────────────┘ │ │
│  │  ┌────────────────────────────────────────────────────────┐ │ │
│  │  │ AuthorizationEngine (decision logic)                   │ │ │
│  │  │  - isAuthorized(principal, operation, resource) → bool │ │ │
│  │  │  - getAuthorizationDigest(principal) → List<...>       │ │ │
│  │  │  - getAuthorizedOperations(principal, resource)        │ │ │
│  │  │  - getAuthorizedResources(principal, operation)        │ │ │
│  │  └────────────────────────────────────────────────────────┘ │ │
│  └──────────────────────────────┬─────────────────────────────┘ │
│                                 │                                │
│  ┌──────────────────────────────▼─────────────────────────────┐ │
│  │           Repository Layer (NEW)                            │ │
│  │  ┌────────────────────────────────────────────────────────┐ │ │
│  │  │ PrincipalRepository                                    │ │ │
│  │  │  - findById(String)                                    │ │ │
│  │  │  - findAll()                                           │ │ │
│  │  │  - findByType(PrincipalType)                           │ │ │
│  │  │  - save(Principal)                                     │ │ │
│  │  │  - delete(String)                                      │ │ │
│  │  └────────────────────────────────────────────────────────┘ │ │
│  │  ┌────────────────────────────────────────────────────────┐ │ │
│  │  │ OperationRepository, ResourceRepository                │ │ │
│  │  │ AccessPredicateRepository                              │ │ │
│  │  │ EdgeRepository (the graph!)                            │ │ │
│  │  └────────────────────────────────────────────────────────┘ │ │
│  └──────────────────────────────┬─────────────────────────────┘ │
│                                 │                                │
└─────────────────────────────────┼────────────────────────────────┘
                                  │
                                  ▼
┌──────────────────────────────────────────────────────────────────┐
│                      DATA ACCESS LAYER                           │
│  (JDBC / JPA / Spring Data - replaces DatabaseHelper)            │
│                                                                  │
│  Option A: Raw JDBC (current)                                    │
│  Option B: Spring Data JPA (recommended for portability)         │
│  Option C: Hibernate / MyBatis                                   │
└──────────────────────────────────────────────────────────────────┘
```

### 3.2 Key Components to Build

#### A. EdgeManager (Core Algorithm)

Replicates `AddEdgeWithSpaceSavings` and `RemoveEdgeWithSpaceSavings`:

```java
@Service
public class EdgeManager {
    
    private final EdgeRepository edgeRepository;
    
    public void addEdge(String start, String end, EdgeSource source) {
        // 1. Check for duplicate (Hops=0 exists)
        if (edgeRepository.existsDirectEdge(start, end, source)) {
            return; // Already exists
        }
        
        // 2. Check for cycle (B→A exists)
        if (start.equals(end) || edgeRepository.existsDirectEdge(end, start, source)) {
            throw new IllegalArgumentException("Circular relation detected!");
        }
        
        // 3. Compute transitive edges
        Set<Edge> candidates = computeTransitiveCandidates(start, end, source);
        
        // 4. Insert direct edge (Hops=0)
        edgeRepository.save(new Edge(start, end, 0, source, false));
        
        // 5. Insert transitive edges (Hops=1)
        for (Edge candidate : candidates) {
            if (!edgeRepository.exists(candidate)) {
                edgeRepository.save(candidate.withHops(1));
            }
        }
    }
    
    private Set<Edge> computeTransitiveCandidates(String start, String end, EdgeSource source) {
        Set<Edge> candidates = new HashSet<>();
        
        // Step 1: A's incoming edges → B
        for (Edge e : edgeRepository.findByEndVertexAndSource(start, source)) {
            candidates.add(new Edge(e.getStartVertex(), end, 1, source, false));
        }
        
        // Step 2: A → B's outgoing edges
        for (Edge e : edgeRepository.findByStartVertexAndSource(end, source)) {
            candidates.add(new Edge(start, e.getEndVertex(), 1, source, false));
        }
        
        // Step 3: A's incoming → B's outgoing
        for (Edge a : edgeRepository.findByEndVertexAndSource(start, source)) {
            for (Edge b : edgeRepository.findByStartVertexAndSource(end, source)) {
                candidates.add(new Edge(a.getStartVertex(), b.getEndVertex(), 1, source, false));
            }
        }
        
        return candidates;
    }
    
    public Set<String> getAncestors(String node, EdgeSource source) {
        // All EndVertex where StartVertex=node
        return edgeRepository.findByStartVertexAndSource(node, source)
            .stream()
            .map(Edge::getEndVertex)
            .collect(Collectors.toSet());
    }
    
    // Similar for removeEdge (replicate RemoveEdgeWithSpaceSavings)
}
```

#### B. AuthorizationEngine

Replicates `IsAuthorized`:

```java
@Service
public class AuthorizationEngine {
    
    private final EdgeManager edgeManager;
    private final AccessPredicateRepository predicateRepo;
    
    public boolean isAuthorized(String principalId, String operationId, String resourceId) {
        // 1. Expand principal (self + all ancestors)
        Set<String> principals = new HashSet<>();
        principals.add(principalId);
        principals.addAll(edgeManager.getAncestors(principalId, EdgeSource.Principal));
        
        // 2. Expand operation (self + all ancestors)
        Set<String> operations = new HashSet<>();
        operations.add(operationId);
        operations.addAll(edgeManager.getAncestors(operationId, EdgeSource.Operation));
        
        // 3. Expand resource (self + all ancestors)
        Set<String> resources = new HashSet<>();
        resources.add(resourceId != null ? resourceId : "");
        resources.addAll(edgeManager.getAncestors(resourceId, EdgeSource.Resource));
        
        // 4. Check any combination in AccessPredicate
        for (String p : principals) {
            for (String o : operations) {
                for (String r : resources) {
                    if (predicateRepo.exists(p, o, r, PredicateType.Grant)) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    public List<ResourceAccessPredicate> getAuthorizationDigest(String principalId) {
        // Similar expansion, but return all matching predicates
        // (uses FlatGrantList view logic or equivalent)
    }
}
```

#### C. Repository Interfaces

```java
public interface EdgeRepository {
    boolean existsDirectEdge(String start, String end, EdgeSource source);
    Set<Edge> findByStartVertexAndSource(String start, EdgeSource source);
    Set<Edge> findByEndVertexAndSource(String end, EdgeSource source);
    void save(Edge edge);
    void delete(Edge edge);
    void deleteByNode(String node, EdgeSource source);
}

public interface AccessPredicateRepository {
    boolean exists(String principal, String operation, String resource, PredicateType type);
    void save(AccessPredicate predicate);
    void delete(AccessPredicate predicate);
    Set<AccessPredicate> findByPrincipal(String principal);
}
```

---

## 4. Migration Strategy

### Phase 1: Parallel Implementation (Low Risk)

```
┌─────────────────────────────────────────────────────────────┐
│                     PHASE 1: PARALLEL                       │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  1. Implement EdgeManager in Java                           │
│     - Unit test with in-memory data structures              │
│     - Verify against existing SQL logic                     │
│                                                             │
│  2. Implement AuthorizationEngine                           │
│     - Port IsAuthorized logic                               │
│     - Port GetAuthorizationDigest, etc.                     │
│                                                             │
│  3. Create Repository interfaces + implementations          │
│     - EdgeRepositoryJdbc (reads/writes Edge table)          │
│     - AccessPredicateRepositoryJdbc                         │
│                                                             │
│  4. Add feature flag:                                       │
│     afcas.engine.mode=sql  (default, current behavior)      │
│     afcas.engine.mode=java (new, application layer)         │
│                                                             │
│  Result: Both engines available, can A/B test               │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### Phase 2: Switch & Validate

```
┌─────────────────────────────────────────────────────────────┐
│                     PHASE 2: SWITCH                         │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  1. Default to java mode in dev/test                        │
│     afcas.engine.mode=java                                  │
│                                                             │
│  2. Run integration tests against both modes                │
│     - Ensure identical results                              │
│                                                             │
│  3. Monitor production with java mode                       │
│     - Compare auth decisions (log diffs)                    │
│                                                             │
│  4. Once stable, remove sql mode option                     │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### Phase 3: Cleanup (Optional)

```
┌─────────────────────────────────────────────────────────────┐
│                     PHASE 3: CLEANUP                        │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  1. Keep SQL procedures as legacy compatibility             │
│     - For CLI tool if it still uses them                    │
│     - For external integrations                             │
│                                                             │
│  2. Or: Deprecate SQL procedures entirely                   │
│     - Java is the single source of truth                    │
│     - Database is just a store                              │
│                                                             │
│  3. Consider adding caching layer                           │
│     - Cache Edge graph in memory (refresh on changes)       │
│     - Cache auth decisions with TTL                         │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## 5. Implementation Steps (Detailed)

### Step 1: Create Repository Layer

```java
// File: src/main/java/com/afcas/repository/EdgeRepository.java
public interface EdgeRepository {
    List<Edge> findAll();
    List<Edge> findBySource(EdgeSource source);
    boolean exists(String start, String end, EdgeSource source, int hops);
    void save(Edge edge);
    void delete(Edge edge);
    void deleteByNode(String node, EdgeSource source);
}

// File: src/main/java/com/afcas/repository/jdbc/JdbcEdgeRepository.java
@Repository
public class JdbcEdgeRepository implements EdgeRepository {
    private final JdbcTemplate jdbc;
    
    @Override
    public List<Edge> findBySource(EdgeSource source) {
        return jdbc.query(
            "SELECT * FROM \"Edge\" WHERE \"Source\" = ?",
            (rs, rowNum) -> mapRow(rs),
            source.toString()
        );
    }
    // ... other methods
}
```

### Step 2: Create EdgeManager

```java
// File: src/main/java/com/afcas/engine/EdgeManager.java
@Component
public class EdgeManager {
    
    private final EdgeRepository edgeRepo;
    
    @Transactional
    public void addEdge(String start, String end, EdgeSource source) {
        // Port logic from AddEdgeWithSpaceSavings
        // (see detailed code above)
    }
    
    @Transactional
    public void removeEdge(String start, String end, EdgeSource source) {
        // Port logic from RemoveEdgeWithSpaceSavings
    }
    
    public Set<String> getAncestors(String node, EdgeSource source) {
        // BFS/DFS through Edge graph
        Set<String> ancestors = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        queue.add(node);
        
        while (!queue.isEmpty()) {
            String current = queue.poll();
            for (Edge e : edgeRepo.findByStartVertexAndSource(current, source)) {
                if (ancestors.add(e.getEndVertex())) {
                    queue.add(e.getEndVertex());
                }
            }
        }
        return ancestors;
    }
}
```

### Step 3: Create AuthorizationEngine

```java
// File: src/main/java/com/afcas/engine/AuthorizationEngine.java
@Component
public class AuthorizationEngine {
    
    private final EdgeManager edgeManager;
    private final AccessPredicateRepository predicateRepo;
    
    public boolean isAuthorized(String principalId, String operationId, String resourceId) {
        Set<String> principals = expandWithAncestors(principalId, EdgeSource.Principal);
        Set<String> operations = expandWithAncestors(operationId, EdgeSource.Operation);
        Set<String> resources = expandWithAncestors(resourceId, EdgeSource.Resource);
        
        for (String p : principals) {
            for (String o : operations) {
                for (String r : resources) {
                    if (predicateRepo.exists(p, o, r, PredicateType.Grant)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private Set<String> expandWithAncestors(String node, EdgeSource source) {
        Set<String> result = new HashSet<>();
        result.add(node);
        result.addAll(edgeManager.getAncestors(node, source));
        return result;
    }
}
```

### Step 4: Wire Up via Feature Flag

```java
// File: src/main/java/com/afcas/config/EngineConfiguration.java
@Configuration
public class EngineConfiguration {
    
    @Value("${afcas.engine.mode:sql}")
    private String engineMode;
    
    @Bean
    public IAuthorizationProvider authorizationProvider(
            AuthorizationEngine javaEngine,
            AuthorizationProvider sqlProvider) {
        
        if ("java".equalsIgnoreCase(engineMode)) {
            return javaEngine;
        }
        return sqlProvider; // default
    }
}
```

### Step 5: Testing

```java
// File: src/test/java/com/afcas/engine/AuthorizationEngineTest.java
@SpringBootTest
class AuthorizationEngineTest {
    
    @Autowired
    private AuthorizationEngine engine;
    
    @Autowired
    private EdgeManager edgeManager;
    
    @Test
    void testIsAuthorized_withHierarchy() {
        // Setup: john in admins, admins has Edit on Workspace
        edgeManager.addEdge("john", "admins", EdgeSource.Principal);
        edgeManager.addEdge("admins", "superusers", EdgeSource.Principal);
        
        // Assert
        assertTrue(engine.isAuthorized("john", "Edit", "Workspace"));
        assertFalse(engine.isAuthorized("john", "Delete", "Workspace"));
    }
}
```

---

## 6. Challenges & Considerations

### 6.1 Edge Transitive Closure is Complex

**Challenge:** The SQL `AddEdgeWithSpaceSavings` is ~150 lines of complex PL/pgSQL.

**Solution:** 
- Port algorithm carefully
- Unit test extensively with graph scenarios
- Consider using a graph library (e.g., JGraphT) for the algorithm

```java
// Using JGraphT for transitive closure
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.alg.TransitiveClosure;

public void addEdgeWithTransitiveClosure(String start, String end, EdgeSource source) {
    // Build in-memory graph from DB
    Graph<String, DefaultEdge> graph = buildGraph(source);
    
    // Add edge
    graph.addEdge(start, end);
    
    // Compute transitive closure
    TransitiveClosure.INSTANCE.closeSimpleDirectedGraph(graph);
    
    // Persist back to DB
    persistGraph(graph, source);
}
```

### 6.2 Performance

**Challenge:** In-memory graph operations may be slower than pre-computed SQL.

**Solution:**
- Cache the Edge graph in memory (refresh on writes)
- Use efficient data structures (HashMap for adjacency lists)
- Benchmark against SQL mode

### 6.3 Concurrency

**Challenge:** Multiple instances modifying edges simultaneously.

**Solution:**
- Use `@Transactional` on write operations
- Database-level locking on Edge table
- Consider optimistic locking with version column

### 6.4 Backward Compatibility

**Challenge:** CLI tool still uses SQL procedures.

**Solution:**
- Keep SQL procedures for CLI
- Or: Update CLI to use the same Java engine via shared library

---

## 7. Summary Checklist

| Task | Priority | Effort |
|------|----------|--------|
| Define EdgeRepository interface | High | Low |
| Implement JdbcEdgeRepository | High | Medium |
| Port AddEdgeWithSpaceSavings to Java | High | High |
| Port RemoveEdgeWithSpaceSavings to Java | High | High |
| Implement AuthorizationEngine.isAuthorized | High | Medium |
| Implement other auth queries (digest, etc.) | Medium | Medium |
| Add feature flag (sql/java mode) | High | Low |
| Write comprehensive unit tests | High | Medium |
| Write integration tests (compare modes) | Medium | Medium |
| Update documentation | Low | Low |

---

## 8. Recommended Starting Point

**Begin with Phase 1, Step 1:** Implement `EdgeManager` with full test coverage using in-memory `List<Edge>` instead of a database. This lets you validate the algorithm logic before touching any persistence.

```java
// Test without DB
@Test
void testTransitiveClosure() {
    EdgeManager em = new EdgeManager(new InMemoryEdgeRepository());
    
    em.addEdge("A", "B", EdgeSource.Principal);
    em.addEdge("B", "C", EdgeSource.Principal);
    
    assertTrue(em.getAncestors("A", EdgeSource.Principal).contains("C"));
}
```

Once the algorithms are proven correct, add the JDBC repository layer.

---

*End of Plan*
