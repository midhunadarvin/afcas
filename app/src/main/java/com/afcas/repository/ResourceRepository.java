package com.afcas.repository;

import com.afcas.objects.Resource;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Resource table operations.
 */
public interface ResourceRepository {

    List<Resource> findAll();

    Optional<Resource> findById(String id);

    Optional<Resource> findByName(String name);

    void save(Resource resource);

    void deleteById(String id);

    boolean existsById(String id);
}
