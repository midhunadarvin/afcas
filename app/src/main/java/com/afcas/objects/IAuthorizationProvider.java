package com.afcas.objects;

import java.sql.SQLException;
import java.util.List;

import com.afcas.objects.ResourceHandle;
import com.afcas.objects.ResourceAccessPredicate;
import com.afcas.objects.Operation;

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
    boolean isSubResource(ResourceHandle resource, ResourceHandle subResource);

    // These two methods are for offline support
    List<ResourceAccessPredicate> getAuthorizationDigest(String principalId);
    List<Operation> getAuthorizedOperations(String principalId, ResourceHandle resource);

    // This can be used to allow the user to browse authorized resources
    List<ResourceHandle> getAuthorizedResources(String principalId, String operationId);
}