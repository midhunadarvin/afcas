package com.afcas.objects;

import java.util.Objects;

/**
 * Represents an edge in the hierarchy graph (transitive closure).
 * Used for Principal, Operation, and Resource hierarchies.
 */
public class Edge {
    private final String startVertex;
    private final String endVertex;
    private final int hops;
    private final EdgeSource source;
    private final boolean delMark;

    public Edge(String startVertex, String endVertex, int hops, EdgeSource source, boolean delMark) {
        this.startVertex = startVertex;
        this.endVertex = endVertex;
        this.hops = hops;
        this.source = source;
        this.delMark = delMark;
    }

    public String getStartVertex() { return startVertex; }
    public String getEndVertex() { return endVertex; }
    public int getHops() { return hops; }
    public EdgeSource getSource() { return source; }
    public boolean isDelMark() { return delMark; }

    public Edge withHops(int newHops) {
        return new Edge(startVertex, endVertex, newHops, source, delMark);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return hops == edge.hops &&
                delMark == edge.delMark &&
                Objects.equals(startVertex, edge.startVertex) &&
                Objects.equals(endVertex, edge.endVertex) &&
                source == edge.source;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startVertex, endVertex, hops, source, delMark);
    }

    @Override
    public String toString() {
        return String.format("Edge{%s -> %s, hops=%d, source=%s, delMark=%s}",
                startVertex, endVertex, hops, source, delMark);
    }
}
