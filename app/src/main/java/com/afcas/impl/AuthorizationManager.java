package com.afcas.impl;

import com.afcas.objects.*;
import com.afcas.utils.CachedRowSetPrinter;
import com.afcas.utils.DatabaseHelper;

import javax.sql.rowset.CachedRowSet;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuthorizationManager implements IAuthorizationManager {

    @Override
    public void addOrUpdate(Principal pr, String source) throws Exception {
        Object[] parameterValues = {
                pr.getName(),
                pr.getPrincipalType().ordinal(),
                pr.getDisplayName(),
                pr.getEmail(),
                pr.getDescription(),
                pr.getDataSource()
        };
        DatabaseHelper.executeStoredProcedure("call \"AddOrUpdatePrincipal\"(?,?,?,?,?,?)", parameterValues);
    }

    @Override
    public void removePrincipal(String id) throws Exception {
        Object[] parameterValues = {
                id
        };
        DatabaseHelper.executeStoredProcedure("call \"RemovePrincipal\"(?)", parameterValues);
    }

    @Override
    public void addOrUpdate(Operation op) throws Exception {
        Object[] parameterValues = {
                op.getId(),
                op.getName(),
                op.getDescription()
        };
        DatabaseHelper.executeStoredProcedure("call \"AddOrUpdateOperation\"(?,?,?)", parameterValues);
    }

    @Override
    public void removeOperation(String id) throws Exception {
        Object[] parameterValues = {
                id
        };
        DatabaseHelper.executeStoredProcedure("call \"RemoveOperation\"(?)", parameterValues);
    }


    @Override
    public void addOrUpdate(Resource resource) throws Exception {
        Object[] parameterValues = {
                resource.getId(),
                resource.getName()
        };
        DatabaseHelper.executeStoredProcedure("call \"AddOrUpdateResource\"(?,?)", parameterValues);
    }

    @Override
    public void removeResource(String id) throws Exception {
        Object[] parameterValues = {
                id
        };
        DatabaseHelper.executeStoredProcedure("call \"RemoveResource\"(?)", parameterValues);
    }

    @Override
    public void addAccessPredicate(ResourceAccessPredicate resourceAccessPredicate) throws Exception {
        Object[] parameterValues = {
                resourceAccessPredicate.getPrincipalId(),
                resourceAccessPredicate.getOperationId(),
                resourceAccessPredicate.getResourceId(),
                resourceAccessPredicate.getAccessPredicateType().ordinal()
        };
        DatabaseHelper.executeStoredProcedure("call \"AddAccessPredicate\"(?,?,?,?)", parameterValues);
    }

    @Override
    public void removeAccessPredicate(ResourceAccessPredicate resourceAccessPredicate) throws Exception {
        Object[] parameterValues = {
                resourceAccessPredicate.getPrincipalId(),
                resourceAccessPredicate.getOperationId(),
                resourceAccessPredicate.getResourceId(),
                resourceAccessPredicate.getAccessPredicateType().ordinal()
        };
        DatabaseHelper.executeStoredProcedure("call \"RemoveAccessPredicate\"(?,?,?,?)", parameterValues);
    }

    @Override
    public void addGroupMember(Principal group, Principal member) throws Exception {
        if (group.getPrincipalType() != PrincipalType.Group) {
            throw new IllegalArgumentException("Only groups may have members");
        }
        Object[] parameterValues = {
                member.getId(),
                group.getId(),
                EdgeSource.Principal.toString()
        };
        DatabaseHelper.executeStoredProcedure("call \"AddEdgeWithSpaceSavings\"(?,?,?)", parameterValues);
    }

    @Override
    public void removeGroupMember(Principal group, Principal member) throws Exception {
        Object[] parameterValues = {
                member.getId(),
                group.getId(),
                EdgeSource.Principal.toString()
        };
        DatabaseHelper.executeStoredProcedure("call \"RemoveEdgeWithSpaceSavings\"(?,?,?)", parameterValues);
    }

    @Override
    public void addSubOperation(Operation parent, Operation subOperation) throws Exception {
        Object[] parameterValues = {
                subOperation.getId(),
                parent.getId(),
                EdgeSource.Operation.toString()
        };
        DatabaseHelper.executeStoredProcedure("call \"AddEdgeWithSpaceSavings\"(?,?,?)", parameterValues);
    }

    @Override
    public void removeSubOperation(Operation parent, Operation subOperation) throws Exception {
        Object[] parameterValues = {
                subOperation.getId(),
                parent.getId(),
                EdgeSource.Operation.toString()
        };
        DatabaseHelper.executeStoredProcedure("call \"RemoveEdgeWithSpaceSavings\"(?,?,?)", parameterValues);
    }

    @Override
    public void addSubResource(Resource resource, Resource subResource) throws Exception {
        Object[] parameterValues = {
                subResource.getId(),
                resource.getId(),
                EdgeSource.Resource.toString()
        };
        DatabaseHelper.executeStoredProcedure("call \"AddEdgeWithSpaceSavings\"(?,?,?)", parameterValues);
    }

    @Override
    public void removeSubResource(Resource resource, Resource subResource) throws Exception {
        Object[] parameterValues = {
                subResource.getId(),
                resource.getId(),
                EdgeSource.Resource.toString()
        };
        DatabaseHelper.executeStoredProcedure("call \"RemoveEdgeWithSpaceSavings\"(?,?,?)", parameterValues);
    }

    @Override
    public List<Principal> getPrincipalList() throws SQLException {
        CachedRowSet result = DatabaseHelper.executeQuery("SELECT * FROM \"GetPrincipalList\"()");
        CachedRowSetPrinter.print(result);
        return buildPrincipalList(result);
    }

    @Override
    public List<Principal> getPrincipalList(PrincipalType type) throws SQLException {
        Object[] parameterValues = {
                type
        };
        CachedRowSet result = DatabaseHelper.executeQuery("SELECT * FROM \"GetPrincipalList\"(?)", parameterValues);
        CachedRowSetPrinter.print(result);
        return buildPrincipalList(result);
    }

    @Override
    public List<Principal> getMembersList(Principal pr) {
        return null;
    }

    @Override
    public List<Principal> getFlatMembersList(Principal pr) {
        return null;
    }

    @Override
    public List<Operation> getOperationList() {
        return null;
    }

    @Override
    public List<Operation> getSubOperationsList(Operation op) {
        return null;
    }

    @Override
    public List<Operation> getFlatSubOperationsList(Operation op) {
        return null;
    }

    private List<Principal> buildPrincipalList(CachedRowSet cachedRowSet) throws SQLException {
        cachedRowSet.first();
        List<Principal> result = new ArrayList<Principal>();

        if (!cachedRowSet.next()) {
            return result;
        }

        do {
            Principal pr = constructPrincipal(cachedRowSet);
            result.add(pr);
        } while (cachedRowSet.next());

        return result;
    }

    private Principal constructPrincipal(CachedRowSet cachedRowSet) throws SQLException {
        return Principal.builder()
                .id(cachedRowSet.getString(1))
                .name(cachedRowSet.getString(2))
                .principalType(PrincipalType.values()[cachedRowSet.getInt(3)])
                .email(cachedRowSet.getString(4))
                .build();
    }
}
