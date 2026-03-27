package com.afcas.repository.jdbc;

import com.afcas.objects.Edge;
import com.afcas.objects.EdgeSource;
import com.afcas.repository.EdgeRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * JDBC implementation of EdgeRepository.
 */
@Repository
public class JdbcEdgeRepository implements EdgeRepository {

    private final JdbcTemplate jdbc;

    public JdbcEdgeRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<Edge> findAll() {
        return jdbc.query("SELECT * FROM \"Edge\"", this::mapRow);
    }

    @Override
    public List<Edge> findBySource(EdgeSource source) {
        return jdbc.query(
            "SELECT * FROM \"Edge\" WHERE \"Source\" = ?",
            this::mapRow,
            source.toString()
        );
    }

    @Override
    public List<Edge> findByStartVertexAndSource(String startVertex, EdgeSource source) {
        return jdbc.query(
            "SELECT * FROM \"Edge\" WHERE \"StartVertex\" = ? AND \"Source\" = ?",
            this::mapRow,
            startVertex, source.toString()
        );
    }

    @Override
    public List<Edge> findByEndVertexAndSource(String endVertex, EdgeSource source) {
        return jdbc.query(
            "SELECT * FROM \"Edge\" WHERE \"EndVertex\" = ? AND \"Source\" = ?",
            this::mapRow,
            endVertex, source.toString()
        );
    }

    @Override
    public Optional<Edge> findByStartEndSource(String startVertex, String endVertex, EdgeSource source, int hops) {
        List<Edge> results = jdbc.query(
            "SELECT * FROM \"Edge\" WHERE \"StartVertex\" = ? AND \"EndVertex\" = ? AND \"Source\" = ? AND \"Hops\" = ?",
            this::mapRow,
            startVertex, endVertex, source.toString(), hops
        );
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public boolean existsDirectEdge(String startVertex, String endVertex, EdgeSource source) {
        Integer count = jdbc.queryForObject(
            "SELECT COUNT(*) FROM \"Edge\" WHERE \"StartVertex\" = ? AND \"EndVertex\" = ? AND \"Source\" = ? AND \"Hops\" = 0",
            Integer.class,
            startVertex, endVertex, source.toString()
        );
        return count != null && count > 0;
    }

    @Override
    public void save(Edge edge) {
        // Check if exists
        Optional<Edge> existing = findByStartEndSource(
            edge.getStartVertex(), edge.getEndVertex(), edge.getSource(), edge.getHops()
        );
        
        if (existing.isPresent()) {
            // Update
            jdbc.update(
                "UPDATE \"Edge\" SET \"DelMark\" = ? WHERE \"StartVertex\" = ? AND \"EndVertex\" = ? AND \"Hops\" = ? AND \"Source\" = ?",
                edge.isDelMark(), edge.getStartVertex(), edge.getEndVertex(), edge.getHops(), edge.getSource().toString()
            );
        } else {
            // Insert
            jdbc.update(
                "INSERT INTO \"Edge\" (\"StartVertex\", \"EndVertex\", \"Hops\", \"Source\", \"DelMark\") VALUES (?, ?, ?, ?, ?)",
                edge.getStartVertex(), edge.getEndVertex(), edge.getHops(), 
                edge.getSource().toString(), edge.isDelMark()
            );
        }
    }

    @Override
    public void delete(Edge edge) {
        jdbc.update(
            "DELETE FROM \"Edge\" WHERE \"StartVertex\" = ? AND \"EndVertex\" = ? AND \"Hops\" = ? AND \"Source\" = ?",
            edge.getStartVertex(), edge.getEndVertex(), edge.getHops(), edge.getSource().toString()
        );
    }

    @Override
    public void deleteByStartVertexAndSource(String startVertex, EdgeSource source) {
        jdbc.update(
            "DELETE FROM \"Edge\" WHERE \"StartVertex\" = ? AND \"Source\" = ?",
            startVertex, source.toString()
        );
    }

    @Override
    public void deleteByEndVertexAndSource(String endVertex, EdgeSource source) {
        jdbc.update(
            "DELETE FROM \"Edge\" WHERE \"EndVertex\" = ? AND \"Source\" = ?",
            endVertex, source.toString()
        );
    }

    private Edge mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Edge(
            rs.getString("StartVertex"),
            rs.getString("EndVertex"),
            rs.getInt("Hops"),
            EdgeSource.valueOf(rs.getString("Source")),
            rs.getBoolean("DelMark")
        );
    }
}
