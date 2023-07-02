package com.afcas.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.afcas.objects.ResourceAccessPredicate;
import com.afcas.objects.ResourceAccessPredicateType;
import com.afcas.objects.ResourceHandle;
import com.afcas.objects.Operation;
import com.afcas.objects.IAuthorizationProvider;
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

//    @Override
//    public boolean isAuthorized(String principalId, String operationId, ResourceHandle resource) throws SQLException {
//        if (principalId == null || principalId.isEmpty()) {
//            throw new IllegalArgumentException("principalId");
//        }
//
//        if (operationId == null || operationId.isEmpty()) {
//            throw new IllegalArgumentException("operationId");
//        }
//
//        if (resource == null) {
//            throw new IllegalArgumentException("resource");
//        }
//
//        return (boolean) DatabaseHelper.executeSQLStatement("IsAuthorized");
//    }

    @Override
    public boolean isMemberOf(String groupId, String memberId) {
        return false;
    }

    @Override
    public boolean isSubOperation(String opId, String subOpId) {
        return false;
    }

    @Override
    public boolean isSubResource(ResourceHandle resource, ResourceHandle subResource) {
        return false;
    }

    @Override
    public List<ResourceAccessPredicate> getAuthorizationDigest(String principalId) {
        return null;
    }

    @Override
    public List<Operation> getAuthorizedOperations(String principalId, ResourceHandle resource) {
        return null;
    }

    @Override
    public List<ResourceHandle> getAuthorizedResources(String principalId, String operationId) {
        return null;
    }
}
