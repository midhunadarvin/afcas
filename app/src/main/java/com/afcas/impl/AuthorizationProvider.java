package com.afcas.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.afcas.objects.*;
import com.afcas.utils.CachedRowSetPrinter;
import com.afcas.utils.DatabaseHelper;

import javax.sql.rowset.CachedRowSet;

public class AuthorizationProvider implements IAuthorizationProvider {

    public AuthorizationProvider() {}

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
    public boolean isSubResource(String resourceId, String subResourceId) throws SQLException {
        if (resourceId == null || resourceId.isEmpty()) {
            throw new IllegalArgumentException("resource");
        }

        if (subResourceId == null || subResourceId.isEmpty()) {
            throw new IllegalArgumentException("subResource");
        }

        Object[] parameterValues = {
                subResourceId,
                resourceId,
                EdgeSource.Resource.toString()
        };

        return (boolean) DatabaseHelper.executeScalar("SELECT * FROM \"EdgeExists\"(?, ?, ?)", parameterValues);
    }

    @Override
    public List<ResourceAccessPredicate> getAuthorizationDigest(String principalId) throws SQLException {
        if (principalId == null || principalId.isEmpty()) {
            throw new IllegalArgumentException("principalId");
        }

        Object[] parameterValues = {
                principalId
        };

        CachedRowSet result = DatabaseHelper.executeQuery("SELECT * FROM \"GetAuthorizationDigest\"(?)", parameterValues);
        CachedRowSetPrinter.print(result);
        List<ResourceAccessPredicate> resourceAccessPredicateList = new ArrayList<>();
        if (result.next()) {
            do {
                String operationName = result.getString(1);
                String resourceName = result.getString(2);
                int accessPredicateType = result.getInt(3);

                ResourceAccessPredicate accessPredicate = ResourceAccessPredicate.builder()
                        .principalId(principalId)
                        .operationId(operationName)
                        .resourceId(resourceName)
                        .accessPredicateType(ResourceAccessPredicateType.values()[accessPredicateType])
                        .build();
                resourceAccessPredicateList.add(accessPredicate);
            } while (result.next());
        }
        return resourceAccessPredicateList;
    }

    @Override
    public List<Operation> getAuthorizedOperations(String principalId, String resourceId) throws SQLException {
        if (principalId == null || principalId.isEmpty()) {
            throw new IllegalArgumentException("principalId");
        }

        if (resourceId == null || resourceId.isEmpty()) {
            throw new IllegalArgumentException("resourceId");
        }

        Object[] parameterValues = {
                principalId,
                resourceId
        };

        CachedRowSet result = DatabaseHelper.executeQuery("SELECT * FROM \"GetAuthorizedOperations\"(?,?)", parameterValues);
        CachedRowSetPrinter.print(result);
        List<Operation> operationList = new ArrayList<>();
        if (result.next()) {
            do {
                String operationName = result.getString(1);

                Operation operation = Operation.builder()
                        .name(operationName)
                        .build();
                operationList.add(operation);
            } while (result.next());
        }
        return operationList;
    }

    @Override
    public List<Resource> getAuthorizedResources(String principalId, String operationId) throws SQLException {
        if (principalId == null || principalId.isEmpty()) {
            throw new IllegalArgumentException("principalId");
        }

        if (operationId == null || operationId.isEmpty()) {
            throw new IllegalArgumentException("operationId");
        }

        Object[] parameterValues = {
                principalId,
                operationId
        };

        CachedRowSet result = DatabaseHelper.executeQuery("SELECT * FROM \"GetAuthorizedResources\"(?,?)", parameterValues);
        CachedRowSetPrinter.print(result);
        List<Resource> resourceList = new ArrayList<>();
        if (result.next()) {
            do {
                String resourceName = result.getString(1);

                Resource resource = Resource.builder()
                        .name(resourceName)
                        .build();
                resourceList.add(resource);
            } while (result.next());
        }
        return resourceList;
    }
}
