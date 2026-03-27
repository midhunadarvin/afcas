package com.afcas.web.service;

import com.afcas.impl.AuthorizationManager;
import com.afcas.impl.AuthorizationProvider;
import com.afcas.objects.*;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class AuthorizationService {

    private final AuthorizationManager authorizationManager;
    private final AuthorizationProvider authorizationProvider;

    public AuthorizationService() {
        this.authorizationManager = new AuthorizationManager();
        this.authorizationProvider = new AuthorizationProvider();
    }

    // Principal operations
    public void addOrUpdatePrincipal(Principal principal, String source) throws Exception {
        authorizationManager.addOrUpdate(principal, source);
    }

    public void removePrincipal(String id) throws Exception {
        authorizationManager.removePrincipal(id);
    }

    public List<Principal> getPrincipalList() throws SQLException {
        return authorizationManager.getPrincipalList();
    }

    public List<Principal> getPrincipalList(PrincipalType type) throws SQLException {
        return authorizationManager.getPrincipalList(type);
    }

    public List<Principal> getMembersList(Principal principal) throws SQLException {
        return authorizationManager.getMembersList(principal);
    }

    public List<Principal> getFlatMembersList(Principal principal) throws SQLException {
        return authorizationManager.getFlatMembersList(principal);
    }

    // Operation operations
    public void addOrUpdateOperation(Operation operation) throws Exception {
        authorizationManager.addOrUpdate(operation);
    }

    public void removeOperation(String id) throws Exception {
        authorizationManager.removeOperation(id);
    }

    public List<Operation> getOperationList() throws SQLException {
        return authorizationManager.getOperationList();
    }

    public List<Operation> getSubOperationsList(Operation operation) throws SQLException {
        return authorizationManager.getSubOperationsList(operation);
    }

    public List<Operation> getFlatSubOperationsList(Operation operation) throws SQLException {
        return authorizationManager.getFlatSubOperationsList(operation);
    }

    // Resource operations
    public void addOrUpdateResource(Resource resource) throws Exception {
        authorizationManager.addOrUpdate(resource);
    }

    public void removeResource(String id) throws Exception {
        authorizationManager.removeResource(id);
    }

    // Access Predicate operations
    public void addAccessPredicate(ResourceAccessPredicate predicate) throws Exception {
        authorizationManager.addAccessPredicate(predicate);
    }

    public void removeAccessPredicate(ResourceAccessPredicate predicate) throws Exception {
        authorizationManager.removeAccessPredicate(predicate);
    }

    // Group membership operations
    public void addGroupMember(Principal group, Principal member) throws Exception {
        authorizationManager.addGroupMember(group, member);
    }

    public void removeGroupMember(Principal group, Principal member) throws Exception {
        authorizationManager.removeGroupMember(group, member);
    }

    // Sub-operation operations
    public void addSubOperation(Operation parent, Operation subOperation) throws Exception {
        authorizationManager.addSubOperation(parent, subOperation);
    }

    public void removeSubOperation(Operation parent, Operation subOperation) throws Exception {
        authorizationManager.removeSubOperation(parent, subOperation);
    }

    // Sub-resource operations
    public void addSubResource(Resource parent, Resource subResource) throws Exception {
        authorizationManager.addSubResource(parent, subResource);
    }

    public void removeSubResource(Resource parent, Resource subResource) throws Exception {
        authorizationManager.removeSubResource(parent, subResource);
    }

    // Authorization checks
    public boolean isAuthorized(String principalId, String operationId, String resourceId) throws SQLException {
        return authorizationProvider.isAuthorized(principalId, operationId, resourceId);
    }

    public boolean isMemberOf(String groupId, String memberId) throws SQLException {
        return authorizationProvider.isMemberOf(groupId, memberId);
    }

    public boolean isSubOperation(String operationId, String subOperationId) throws SQLException {
        return authorizationProvider.isSubOperation(operationId, subOperationId);
    }

    public boolean isSubResource(String resourceId, String subResourceId) throws SQLException {
        return authorizationProvider.isSubResource(resourceId, subResourceId);
    }

    // Authorization digest and queries
    public List<ResourceAccessPredicate> getAuthorizationDigest(String principalId) throws SQLException {
        return authorizationProvider.getAuthorizationDigest(principalId);
    }

    public List<Operation> getAuthorizedOperations(String principalId, String resourceId) throws SQLException {
        return authorizationProvider.getAuthorizedOperations(principalId, resourceId);
    }

    public List<Resource> getAuthorizedResources(String principalId, String operationId) throws SQLException {
        return authorizationProvider.getAuthorizedResources(principalId, operationId);
    }
}
