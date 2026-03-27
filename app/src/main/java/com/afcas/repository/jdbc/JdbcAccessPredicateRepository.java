package com.afcas.repository.jdbc;

import com.afcas.objects.ResourceAccessPredicate;
import com.afcas.objects.ResourceAccessPredicateType;
import com.afcas.repository.AccessPredicateRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcAccessPredicateRepository implements AccessPredicateRepository {

    private final JdbcTemplate jdbc;

    public JdbcAccessPredicateRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<ResourceAccessPredicate> findAll() {
        return jdbc.query("SELECT * FROM \"AccessPredicate\"", this::mapRow);
    }

    @Override
    public List<ResourceAccessPredicate> findByPrincipalId(String principalId) {
        return jdbc.query(
            "SELECT * FROM \"AccessPredicate\" WHERE \"PrincipalId\" = ?",
            this::mapRow, principalId
        );
    }

    @Override
    public Optional<ResourceAccessPredicate> findByKey(String principalId, String operationId, 
                                                        String resourceId, ResourceAccessPredicateType type) {
        List<ResourceAccessPredicate> results = jdbc.query(
            "SELECT * FROM \"AccessPredicate\" WHERE \"PrincipalId\" = ? AND \"OperationId\" = ? AND \"ResourceId\" = ? AND \"PredicateType\" = ?",
            this::mapRow, principalId, operationId, resourceId, type.ordinal()
        );
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public boolean exists(String principalId, String operationId, String resourceId, 
                          ResourceAccessPredicateType type) {
        Integer count = jdbc.queryForObject(
            "SELECT COUNT(*) FROM \"AccessPredicate\" WHERE \"PrincipalId\" = ? AND \"OperationId\" = ? AND \"ResourceId\" = ? AND \"PredicateType\" = ?",
            Integer.class, principalId, operationId, resourceId, type.ordinal()
        );
        return count != null && count > 0;
    }

    @Override
    public void save(ResourceAccessPredicate predicate) {
        // Delete first (upsert pattern for composite key)
        jdbc.update(
            "DELETE FROM \"AccessPredicate\" WHERE \"PrincipalId\" = ? AND \"OperationId\" = ? AND \"ResourceId\" = ? AND \"PredicateType\" = ?",
            predicate.getPrincipalId(), predicate.getOperationId(), 
            predicate.getResourceId(), predicate.getAccessPredicateType().ordinal()
        );
        // Insert
        jdbc.update(
            "INSERT INTO \"AccessPredicate\" (\"PrincipalId\", \"OperationId\", \"ResourceId\", \"PredicateType\") VALUES (?, ?, ?, ?)",
            predicate.getPrincipalId(), predicate.getOperationId(), 
            predicate.getResourceId(), predicate.getAccessPredicateType().ordinal()
        );
    }

    @Override
    public void delete(ResourceAccessPredicate predicate) {
        jdbc.update(
            "DELETE FROM \"AccessPredicate\" WHERE \"PrincipalId\" = ? AND \"OperationId\" = ? AND \"ResourceId\" = ? AND \"PredicateType\" = ?",
            predicate.getPrincipalId(), predicate.getOperationId(), 
            predicate.getResourceId(), predicate.getAccessPredicateType().ordinal()
        );
    }

    @Override
    public void deleteByPrincipalId(String principalId) {
        jdbc.update("DELETE FROM \"AccessPredicate\" WHERE \"PrincipalId\" = ?", principalId);
    }

    private ResourceAccessPredicate mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ResourceAccessPredicate.builder()
            .principalId(rs.getString("PrincipalId"))
            .operationId(rs.getString("OperationId"))
            .resourceId(rs.getString("ResourceId"))
            .accessPredicateType(ResourceAccessPredicateType.values()[rs.getInt("PredicateType")])
            .build();
    }
}
