package com.afcas.commands;

import com.afcas.impl.AuthorizationProvider;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(
        name = "authorized-resources",
        description = "gets the authorized resources"
)
public class GetAuthorizedResources implements Runnable {

    private AuthorizationProvider authorizationProvider;

    @Parameters(index = "0", description = "The principal name", arity = "1")
    private String principalId;

    @Parameters(index = "1", description = "The resource name", arity = "1")
    private String resourceId;

    public GetAuthorizedResources() {
        this.authorizationProvider = new AuthorizationProvider(3600);
    }

    @Override
    public void run() {

        try {
            authorizationProvider.getAuthorizedResources(principalId, resourceId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
