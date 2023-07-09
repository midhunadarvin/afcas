package com.afcas.impl;

import java.sql.SQLException;
import java.util.List;

import com.afcas.objects.*;
import com.afcas.utils.DatabaseHelper;

public class AuthorizationProvider implements IAuthorizationProvider {
    private static final String _CacheKeyPrefix = "AuthorizationProvider.";
    private final int _CacheDurationInSeconds;

    public AuthorizationProvider(int cacheDurationInSeconds) {
        _CacheDurationInSeconds = cacheDurationInSeconds;
    }

    public int getCacheDurationInSeconds() {
        return _CacheDurationInSeconds;
    }

    @Override
    public boolean isAuthorized(String principalId, String operationId, String resourceId) throws SQLException {
        if (principalId == null || principalId.isEmpty()) {
            throw new IllegalArgumentException("principalId");
        }

        if (operationId == null || operationId.isEmpty()) {
            throw new IllegalArgumentException("operationId");
        }

        if (resourceId == null) {
            throw new IllegalArgumentException("resourceId");
        }

        Object[] parameterValues = {
                principalId,
                operationId,
                resourceId
        };

        return (boolean) DatabaseHelper.executeScalar("SELECT * FROM \"IsAuthorized\"(?, ?, ?)", parameterValues);
    }

    @Override
    public boolean isMemberOf(String groupId, String memberId) throws SQLException {
        if (groupId == null || groupId.isEmpty()) {
            throw new IllegalArgumentException("groupId");
        }

        if (memberId == null || memberId.isEmpty()) {
            throw new IllegalArgumentException("memberId");
        }

        Object[] parameterValues = {
                memberId,
                groupId,
                EdgeSource.Principal.toString()
        };

        return (boolean) DatabaseHelper.executeScalar("SELECT * FROM \"EdgeExists\"(?, ?, ?)", parameterValues);
    }

    @Override
    public boolean isSubOperation(String operationId, String subOperationId) throws SQLException {
        if (operationId == null || operationId.isEmpty()) {
            throw new IllegalArgumentException("operationId");
        }

        if (subOperationId == null || subOperationId.isEmpty()) {
            throw new IllegalArgumentException("subOperationId");
        }

        Object[] parameterValues = {
                subOperationId,
                operationId,
                EdgeSource.Operation.toString()
        };

        return (boolean) DatabaseHelper.executeScalar("SELECT * FROM \"EdgeExists\"(?, ?, ?)", parameterValues);
    }

    @Override
    public boolean isSubResource(Resource resource, Resource subResource) {
        return false;
    }

    @Override
    public List<ResourceAccessPredicate> getAuthorizationDigest(String principalId) {
        return null;
    }

    @Override
    public List<Operation> getAuthorizedOperations(String principalId, Resource resource) {
        return null;
    }

    @Override
    public List<Resource> getAuthorizedResources(String principalId, String operationId) {
        return null;
    }
}
