package com.afcas.impl;

import com.afcas.objects.*;
import com.afcas.utils.DatabaseHelper;

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
    public void removeAccessPredicate(String principalId, String operationId, Resource resource, ResourceAccessPredicateType type) {

    }

    @Override
    public void addGroupMember(Principal group, Principal member) {

    }

    @Override
    public void removeGroupMember(Principal group, Principal member) {

    }

    @Override
    public void addSubOperation(Operation parent, Operation subOperation) {

    }

    @Override
    public void removeSubOperation(Operation parent, Operation subOperation) {

    }

    @Override
    public void addSubResource(Resource resource, Resource subResource) {

    }

    @Override
    public void removeSubResource(Resource resource, Resource subResource) {

    }

    @Override
    public List<Principal> getPrincipalList() {
        return null;
    }

    @Override
    public List<Principal> getPrincipalList(PrincipalType type) {
        return null;
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

    @Override
    public boolean isMemberOf(String groupId, String memberId) {
        return false;
    }

    @Override
    public boolean isSubOperation(String opId, String subOpId) {
        return false;
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
