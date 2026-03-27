package com.afcas.engine;

import com.afcas.objects.*;
import com.afcas.objects.IAuthorizationProvider;
import com.afcas.repository.AccessPredicateRepository;
import com.afcas.repository.EdgeRepository;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Application-layer authorization engine.
 * 
 * This is a Java port of the PostgreSQL IsAuthorized, GetAuthorizationDigest,
 * GetAuthorizedOperations, and GetAuthorizedResources functions.
 */
@Service
public class AuthorizationEngine implements IAuthorizationProvider {

    private final EdgeManager edgeManager;
    private final AccessPredicateRepository predicateRepository;

    public AuthorizationEngine(EdgeManager edgeManager, AccessPredicateRepository predicateRepository) {
        this.edgeManager = edgeManager;
        this.predicateRepository = predicateRepository;
    }

    /**
     * Check if a principal is authorized to perform an operation on a resource.
     * Port of IsAuthorized stored function.
     */
    @Override
    public boolean isAuthorized(String principalId, String operationId, String resourceId) throws java.sql.SQLException {
        if (principalId == null || principalId.isEmpty()) {
            throw new IllegalArgumentException("principalId");
        }
        if (operationId == null || operationId.isEmpty()) {
            throw new IllegalArgumentException("operationId");
        }
        // resourceId can be null/empty

        // 1. Expand principal (self + all ancestors)
        Set<String> principals = expandWithAncestors(principalId, EdgeSource.Principal);

        // 2. Expand operation (self + all ancestors)
        Set<String> operations = expandWithAncestors(operationId, EdgeSource.Operation);

        // 3. Expand resource (self + all ancestors, handle null)
        Set<String> resources = new HashSet<>();
        if (resourceId == null || resourceId.isEmpty()) {
            resources.add("");
        } else {
            resources.add(resourceId);
            resources.addAll(edgeManager.getAncestors(resourceId, EdgeSource.Resource));
        }

        // 4. Check if any combination exists in AccessPredicate with Grant
        for (String p : principals) {
            for (String o : operations) {
                for (String r : resources) {
                    if (predicateRepository.exists(p, o, r, ResourceAccessPredicateType.Grant)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Get all authorization predicates for a principal (authorization digest).
     * Port of GetAuthorizationDigest.
     */
    @Override
    public List<ResourceAccessPredicate> getAuthorizationDigest(String principalId) throws java.sql.SQLException {
        if (principalId == null || principalId.isEmpty()) {
            throw new IllegalArgumentException("principalId");
        }

        // Get all ancestors of this principal
        Set<String> principals = expandWithAncestors(principalId, EdgeSource.Principal);
        principals.add(principalId); // Include self

        // Find all predicates where principal (or ancestor) has access
        List<ResourceAccessPredicate> result = new ArrayList<>();
        for (ResourceAccessPredicate predicate : predicateRepository.findAll()) {
            if (principals.contains(predicate.getPrincipalId())) {
                result.add(predicate);
            }
        }

        return result;
    }

    /**
     * Get operations a principal can perform on a resource.
     * Port of GetAuthorizedOperations.
     */
    @Override
    public List<Operation> getAuthorizedOperations(String principalId, String resourceId) throws java.sql.SQLException {
        if (principalId == null || principalId.isEmpty()) {
            throw new IllegalArgumentException("principalId");
        }
        if (resourceId == null || resourceId.isEmpty()) {
            throw new IllegalArgumentException("resourceId");
        }

        // Expand principal
        Set<String> principals = expandWithAncestors(principalId, EdgeSource.Principal);
        principals.add(principalId);

        // Expand resource
        Set<String> resources = expandWithAncestors(resourceId, EdgeSource.Resource);
        resources.add(resourceId);

        // Find all operations granted to any (principal, resource) combination
        Set<String> operationIds = new HashSet<>();
        for (ResourceAccessPredicate predicate : predicateRepository.findAll()) {
            if (predicate.getAccessPredicateType() == ResourceAccessPredicateType.Grant &&
                principals.contains(predicate.getPrincipalId()) &&
                resources.contains(predicate.getResourceId())) {
                operationIds.add(predicate.getOperationId());
            }
        }

        // Convert to Operation objects
        List<Operation> result = new ArrayList<>();
        for (String opId : operationIds) {
            result.add(Operation.builder().id(opId).name(opId).build());
        }
        return result;
    }

    /**
     * Get resources a principal can access with a specific operation.
     * Port of GetAuthorizedResources.
     */
    @Override
    public List<Resource> getAuthorizedResources(String principalId, String operationId) throws java.sql.SQLException {
        if (principalId == null || principalId.isEmpty()) {
            throw new IllegalArgumentException("principalId");
        }
        if (operationId == null || operationId.isEmpty()) {
            throw new IllegalArgumentException("operationId");
        }

        // Expand principal
        Set<String> principals = expandWithAncestors(principalId, EdgeSource.Principal);
        principals.add(principalId);

        // Expand operation
        Set<String> operations = expandWithAncestors(operationId, EdgeSource.Operation);
        operations.add(operationId);

        // Find all resources granted to any (principal, operation) combination
        Set<String> resourceIds = new HashSet<>();
        for (ResourceAccessPredicate predicate : predicateRepository.findAll()) {
            if (predicate.getAccessPredicateType() == ResourceAccessPredicateType.Grant &&
                principals.contains(predicate.getPrincipalId()) &&
                operations.contains(predicate.getOperationId())) {
                resourceIds.add(predicate.getResourceId());
            }
        }

        // Convert to Resource objects
        List<Resource> result = new ArrayList<>();
        for (String resId : resourceIds) {
            result.add(Resource.builder().id(resId).name(resId).build());
        }
        return result;
    }

    /**
     * Check if a member is in a group.
     * Port of isMemberOf.
     */
    @Override
    public boolean isMemberOf(String groupId, String memberId) throws java.sql.SQLException {
        if (groupId == null || groupId.isEmpty()) {
            throw new IllegalArgumentException("groupId");
        }
        if (memberId == null || memberId.isEmpty()) {
            throw new IllegalArgumentException("memberId");
        }

        // Check if there's an edge from member to group (directly or transitively)
        Set<String> ancestors = edgeManager.getAncestors(memberId, EdgeSource.Principal);
        return ancestors.contains(groupId);
    }

    /**
     * Check if an operation is a sub-operation of another.
     * Port of isSubOperation.
     */
    @Override
    public boolean isSubOperation(String operationId, String subOperationId) throws java.sql.SQLException {
        if (operationId == null || operationId.isEmpty()) {
            throw new IllegalArgumentException("operationId");
        }
        if (subOperationId == null || subOperationId.isEmpty()) {
            throw new IllegalArgumentException("subOperationId");
        }

        Set<String> ancestors = edgeManager.getAncestors(subOperationId, EdgeSource.Operation);
        return ancestors.contains(operationId);
    }

    /**
     * Check if a resource is a sub-resource of another.
     * Port of isSubResource.
     */
    @Override
    public boolean isSubResource(String resourceId, String subResourceId) throws java.sql.SQLException {
        if (resourceId == null || resourceId.isEmpty()) {
            throw new IllegalArgumentException("resourceId");
        }
        if (subResourceId == null || subResourceId.isEmpty()) {
            throw new IllegalArgumentException("subResourceId");
        }

        Set<String> ancestors = edgeManager.getAncestors(subResourceId, EdgeSource.Resource);
        return ancestors.contains(resourceId);
    }

    /**
     * Expand a node to include itself and all ancestors.
     */
    private Set<String> expandWithAncestors(String node, EdgeSource source) {
        Set<String> result = new HashSet<>();
        result.add(node);
        result.addAll(edgeManager.getAncestors(node, source));
        return result;
    }
}
