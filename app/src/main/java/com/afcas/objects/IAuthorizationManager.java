package com.afcas.objects;

import java.sql.SQLException;
import java.util.List;

public interface IAuthorizationManager extends IAuthorizationProvider {

    // CRUD for principals
    Object addOrUpdate(Principal pr, String source);
    void removePrincipal(String id);

    // CRUD for operations
    Object addOrUpdate(Operation pr);
    void removeOperation(String id);

    // These two methods are for maintaining the ACL
    void addAccessPredicate(String principalId, String operationId, ResourceHandle resource, ResourceAccessPredicateType type);
    void removeAccessPredicate(String principalId, String operationId, ResourceHandle resource, ResourceAccessPredicateType type);

    // These are to maintain the hierarchy of principal, operation, and resources
    void addGroupMember(Principal group, Principal member);
    void removeGroupMember(Principal group, Principal member);
    void addSubOperation(Operation parent, Operation subOperation);
    void removeSubOperation(Operation parent, Operation subOperation);
    void addSubResource(ResourceHandle resource, ResourceHandle subResource);
    void removeSubResource(ResourceHandle resource, ResourceHandle subResource);

    // These are for listing purposes
    List<Principal> getPrincipalList();
    List<Principal> getPrincipalList(PrincipalType type);
    List<Principal> getMembersList(Principal pr);
    List<Principal> getFlatMembersList(Principal pr);

    List<Operation> getOperationList();
    List<Operation> getSubOperationsList(Operation op);
    List<Operation> getFlatSubOperationsList(Operation op);
}
