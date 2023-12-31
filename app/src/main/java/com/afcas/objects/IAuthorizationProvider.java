package com.afcas.objects;

import java.sql.SQLException;
import java.util.List;

/**
 * The main interface to be used by the clients that need to make authorization decisions.
 * An instance of this interface is provided by the Afcas class.
 */
public interface IAuthorizationProvider {
    // the method to justify the existence of this interface
    boolean isAuthorized(String principalId, String operationId, String resourceId) throws SQLException;

    // these methods also have uses for authorization purposes
    boolean isMemberOf(String groupId, String memberId) throws SQLException;
    boolean isSubOperation(String opId, String subOpId) throws SQLException;
    boolean isSubResource(String resourceId, String subResourceId) throws SQLException;

    // These two methods are for offline support
    List<ResourceAccessPredicate> getAuthorizationDigest(String principalId) throws SQLException;
    List<Operation> getAuthorizedOperations(String principalId, String resourceId) throws SQLException;

    // This can be used to allow the user to browse authorized resources
    List<Resource> getAuthorizedResources(String principalId, String operationId) throws SQLException;
}