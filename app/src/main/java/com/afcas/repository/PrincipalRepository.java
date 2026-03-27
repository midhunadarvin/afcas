package com.afcas.repository;

import com.afcas.objects.Principal;
import com.afcas.objects.PrincipalType;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Principal table operations.
 */
public interface PrincipalRepository {

    List<Principal> findAll();

    List<Principal> findByType(PrincipalType type);

    Optional<Principal> findById(String id);

    Optional<Principal> findByName(String name);

    void save(Principal principal);

    void deleteById(String id);

    boolean existsByName(String name);
}
