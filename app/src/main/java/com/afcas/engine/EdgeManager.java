package com.afcas.engine;

import com.afcas.objects.Edge;
import com.afcas.objects.EdgeSource;
import com.afcas.repository.EdgeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Manages hierarchical edge relationships with transitive closure.
 * 
 * This is a Java port of the PostgreSQL AddEdgeWithSpaceSavings and 
 * RemoveEdgeWithSpaceSavings procedures.
 */
@Service
public class EdgeManager {

    private final EdgeRepository edgeRepository;

    public EdgeManager(EdgeRepository edgeRepository) {
        this.edgeRepository = edgeRepository;
    }

    /**
     * Add an edge with automatic transitive closure computation.
     * Port of AddEdgeWithSpaceSavings stored procedure.
     */
    @Transactional
    public void addEdge(String startVertexId, String endVertexId, EdgeSource source) {
        // Step 1: Check for duplicate direct edge
        if (edgeRepository.existsDirectEdge(startVertexId, endVertexId, source)) {
            return; // Already exists, do nothing
        }

        // Step 2: Check for cycle (self-loop or reverse edge exists)
        if (startVertexId.equals(endVertexId)) {
            throw new IllegalArgumentException("Attempt to create a circular relation detected!");
        }
        if (edgeRepository.existsDirectEdge(endVertexId, startVertexId, source)) {
            throw new IllegalArgumentException("Attempt to create a circular relation detected!");
        }

        // Step 3: Compute transitive candidates
        Set<Edge> candidates = computeTransitiveCandidates(startVertexId, endVertexId, source);

        // Step 4: Insert direct edge (Hops=0)
        edgeRepository.save(new Edge(startVertexId, endVertexId, 0, source, false));

        // Step 5: Insert transitive edges (Hops=1), avoiding duplicates
        for (Edge candidate : candidates) {
            if (!edgeRepository.existsDirectEdge(candidate.getStartVertex(), candidate.getEndVertex(), source)) {
                // Only insert if not already a direct edge
                edgeRepository.save(candidate);
            }
        }
    }

    /**
     * Compute transitive edge candidates.
     * Port of the candidate computation in AddEdgeWithSpaceSavings.
     */
    private Set<Edge> computeTransitiveCandidates(String startVertexId, String endVertexId, EdgeSource source) {
        Set<Edge> candidates = new HashSet<>();

        // Step 1: A's incoming edges → B (all parents of start → end)
        List<Edge> incomingToStart = edgeRepository.findByEndVertexAndSource(startVertexId, source);
        for (Edge e : incomingToStart) {
            candidates.add(new Edge(e.getStartVertex(), endVertexId, 1, source, false));
        }

        // Step 2: A → B's outgoing edges (start → all children of end)
        List<Edge> outgoingFromEnd = edgeRepository.findByStartVertexAndSource(endVertexId, source);
        for (Edge e : outgoingFromEnd) {
            candidates.add(new Edge(startVertexId, e.getEndVertex(), 1, source, false));
        }

        // Step 3: A's incoming → end vertex of B's outgoing
        for (Edge a : incomingToStart) {
            for (Edge b : outgoingFromEnd) {
                candidates.add(new Edge(a.getStartVertex(), b.getEndVertex(), 1, source, false));
            }
        }

        return candidates;
    }

    /**
     * Remove an edge and update transitive edges.
     * Port of RemoveEdgeWithSpaceSavings stored procedure.
     */
    @Transactional
    public void removeEdge(String startVertexId, String endVertexId, EdgeSource source) {
        // Check if direct edge exists
        if (!edgeRepository.existsDirectEdge(startVertexId, endVertexId, source)) {
            return; // Nothing to delete
        }

        // Delete the direct edge
        edgeRepository.delete(new Edge(startVertexId, endVertexId, 0, source, false));

        // Mark transitive edges for deletion (simplified version)
        // Full implementation would recompute which transitive edges are still valid
        List<Edge> transitiveEdges = edgeRepository.findByStartVertexAndSource(startVertexId, source);
        for (Edge e : transitiveEdges) {
            if (e.getHops() > 0 && e.getEndVertex().equals(endVertexId)) {
                edgeRepository.delete(e);
            }
        }
    }

    /**
     * Get all ancestors of a node (following the direction of edges).
     * Ancestors are nodes reachable by following EndVertex from StartVertex.
     */
    public Set<String> getAncestors(String node, EdgeSource source) {
        Set<String> ancestors = new HashSet<>();
        Set<String> visited = new HashSet<>();
        collectAncestors(node, source, ancestors, visited);
        return ancestors;
    }

    private void collectAncestors(String node, EdgeSource source, Set<String> ancestors, Set<String> visited) {
        if (visited.contains(node)) return;
        visited.add(node);

        for (Edge e : edgeRepository.findByStartVertexAndSource(node, source)) {
            if (ancestors.add(e.getEndVertex())) {
                collectAncestors(e.getEndVertex(), source, ancestors, visited);
            }
        }
    }

    /**
     * Get all descendants of a node (following the reverse direction of edges).
     */
    public Set<String> getDescendants(String node, EdgeSource source) {
        Set<String> descendants = new HashSet<>();
        Set<String> visited = new HashSet<>();
        collectDescendants(node, source, descendants, visited);
        return descendants;
    }

    private void collectDescendants(String node, EdgeSource source, Set<String> descendants, Set<String> visited) {
        if (visited.contains(node)) return;
        visited.add(node);

        for (Edge e : edgeRepository.findByEndVertexAndSource(node, source)) {
            if (descendants.add(e.getStartVertex())) {
                collectDescendants(e.getStartVertex(), source, descendants, visited);
            }
        }
    }

    /**
     * Check if a direct edge exists.
     */
    public boolean edgeExists(String start, String end, EdgeSource source) {
        return edgeRepository.existsDirectEdge(start, end, source);
    }
}
