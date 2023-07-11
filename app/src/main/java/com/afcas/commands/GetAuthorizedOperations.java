package com.afcas.commands;

import com.afcas.impl.AuthorizationProvider;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;

@Command(
        name = "authorized-operations",
        description = "gets the authorized operations"
)
public class GetAuthorizedOperations implements Runnable {

    private AuthorizationProvider authorizationProvider;

    @Parameters(index = "0", description = "The principal name", arity = "1")
    private String principalId;

    @Parameters(index = "1", description = "The resource name", arity = "1")
    private String resourceId;

    public GetAuthorizedOperations() {
        this.authorizationProvider = new AuthorizationProvider();
    }

    @Override
    public void run() {

        try {
            authorizationProvider.getAuthorizedOperations(principalId, resourceId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
