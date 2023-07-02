package com.afcas.impl;

import com.afcas.objects.Principal;
import com.afcas.objects.IAuthorizationManager;
import com.afcas.objects.ResourceHandle;
import com.afcas.objects.ResourceAccessPredicateType;
import com.afcas.objects.Operation;
import com.afcas.objects.PrincipalType;
import com.afcas.objects.ResourceAccessPredicate;
import com.afcas.utils.DatabaseHelper;

import java.util.List;

public class AuthorizationManager implements IAuthorizationManager {

    @Override
    public Object addOrUpdate(Principal pr, String source) {
        try {
            Object[] parameterValues = {
                    pr.getName(),
                    pr.getPrincipalType().ordinal(),
                    pr.getDisplayName(),
                    pr.getEmail(),
                    pr.getDescription(),
                    pr.getDataSource()
            };
            return DatabaseHelper.executeStoredProcedure("call \"AddOrUpdatePrincipal\"(?,?,?,?,?,?)", parameterValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void removePrincipal(String id) {

    }

    @Override
    public Object addOrUpdate(Operation op) {
        try {
            Object[] parameterValues = {
                    op.getId(),
                    op.getName(),
                    op.getDescription()
            };
            return DatabaseHelper.executeStoredProcedure("call \"AddOrUpdateOperation\"(?,?,?)", parameterValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public void removeOperation(String id) {

    }

    @Override
    public void addAccessPredicate(String principalId, String operationId, ResourceHandle resource, ResourceAccessPredicateType type) {

    }

    @Override
    public void removeAccessPredicate(String principalId, String operationId, ResourceHandle resource, ResourceAccessPredicateType type) {

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
    public void addSubResource(ResourceHandle resource, ResourceHandle subResource) {

    }

    @Override
    public void removeSubResource(ResourceHandle resource, ResourceHandle subResource) {

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
