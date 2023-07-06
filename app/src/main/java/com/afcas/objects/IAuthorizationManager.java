package com.afcas.objects;

import java.util.List;

public interface IAuthorizationManager extends IAuthorizationProvider {

    // CRUD for principals
    Object addOrUpdate(Principal pr, String source) throws Exception;
    Object removePrincipal(String id) throws Exception;

    // CRUD for operations
    Object addOrUpdate(Operation pr) throws Exception;
    Object removeOperation(String id) throws Exception;

    // CRUD for resources
    Object addOrUpdate(Resource resource) throws Exception;
    Object removeResource(String id) throws Exception;

    // These two methods are for maintaining the ACL
    void addAccessPredicate(String principalId, String operationId, Resource resource, ResourceAccessPredicateType type);
    void removeAccessPredicate(String principalId, String operationId, Resource resource, ResourceAccessPredicateType type);

    // These are to maintain the hierarchy of principal, operation, and resources
    void addGroupMember(Principal group, Principal member);
    void removeGroupMember(Principal group, Principal member);
    void addSubOperation(Operation parent, Operation subOperation);
    void removeSubOperation(Operation parent, Operation subOperation);
    void addSubResource(Resource resource, Resource subResource);
    void removeSubResource(Resource resource, Resource subResource);

    // These are for listing purposes
    List<Principal> getPrincipalList();
    List<Principal> getPrincipalList(PrincipalType type);
    List<Principal> getMembersList(Principal pr);
    List<Principal> getFlatMembersList(Principal pr);

    List<Operation> getOperationList();
    List<Operation> getSubOperationsList(Operation op);
    List<Operation> getFlatSubOperationsList(Operation op);
}
