package com.afcas.repository;

import com.afcas.objects.Edge;
import com.afcas.objects.EdgeSource;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Edge table operations (hierarchy graph).
 */
public interface EdgeRepository {

    List<Edge> findAll();

    List<Edge> findBySource(EdgeSource source);

    List<Edge> findByStartVertexAndSource(String startVertex, EdgeSource source);

    List<Edge> findByEndVertexAndSource(String endVertex, EdgeSource source);

    Optional<Edge> findByStartEndSource(String startVertex, String endVertex, EdgeSource source, int hops);

    boolean existsDirectEdge(String startVertex, String endVertex, EdgeSource source);

    void save(Edge edge);

    void delete(Edge edge);

    void deleteByStartVertexAndSource(String startVertex, EdgeSource source);

    void deleteByEndVertexAndSource(String endVertex, EdgeSource source);
}
