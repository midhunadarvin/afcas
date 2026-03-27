package com.afcas.repository;

import com.afcas.objects.ResourceAccessPredicate;
import com.afcas.objects.ResourceAccessPredicateType;

import java.util.List;
import java.util.Optional;

/**
 * Repository for AccessPredicate table operations.
 */
public interface AccessPredicateRepository {

    List<ResourceAccessPredicate> findAll();

    List<ResourceAccessPredicate> findByPrincipalId(String principalId);

    Optional<ResourceAccessPredicate> findByKey(String principalId, String operationId, 
                                                  String resourceId, ResourceAccessPredicateType type);

    boolean exists(String principalId, String operationId, String resourceId, 
                   ResourceAccessPredicateType type);

    void save(ResourceAccessPredicate predicate);

    void delete(ResourceAccessPredicate predicate);

    void deleteByPrincipalId(String principalId);
}
