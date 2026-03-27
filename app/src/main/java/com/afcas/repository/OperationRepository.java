package com.afcas.repository;

import com.afcas.objects.Operation;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Operation table operations.
 */
public interface OperationRepository {

    List<Operation> findAll();

    Optional<Operation> findById(String id);

    Optional<Operation> findByName(String name);

    void save(Operation operation);

    void deleteById(String id);

    boolean existsById(String id);
}
