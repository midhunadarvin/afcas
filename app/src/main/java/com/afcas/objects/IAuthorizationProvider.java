package com.afcas.objects;

import java.util.List;

/**
 * The main interface to be used by the clients that need to make authorization decisions.
 * An instance of this interface is provided by the Afcas class.
 */
public interface IAuthorizationProvider {
    // the method to justify the existence of this interface
//    boolean isAuthorized(String principalId, String operationId, ResourceHandle resource) throws SQLException;

    // these methods also have uses for authorization purposes
    boolean isMemberOf(String groupId, String memberId);
    boolean isSubOperation(String opId, String subOpId);
    boolean isSubResource(Resource resource, Resource subResource);

    // These two methods are for offline support
    List<ResourceAccessPredicate> getAuthorizationDigest(String principalId);
    List<Operation> getAuthorizedOperations(String principalId, Resource resource);

    // This can be used to allow the user to browse authorized resources
    List<Resource> getAuthorizedResources(String principalId, String operationId);
}