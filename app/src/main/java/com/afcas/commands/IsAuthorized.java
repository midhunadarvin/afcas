package com.afcas.commands;

import com.afcas.impl.AuthorizationProvider;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(
        name = "is-authorized",
        description = "Check if the specified entity has operation permissions to the resource"
)
public class IsAuthorized implements Runnable {

    private AuthorizationProvider authorizationProvider;

    @Parameters(index = "0", description = "The Principal name", arity = "1")
    private String principleId;

    @Parameters(index = "1", description = "The operation name", arity = "1")
    private String operationId;

    @Parameters(index = "2", description = "The resource name", arity = "1")
    private String resourceId;

    public IsAuthorized() {
        this.authorizationProvider = new AuthorizationProvider(3600);
    }

    @Override
    public void run() {
        try {
            Boolean isAuthorized = authorizationProvider.isAuthorized(principleId, operationId, resourceId);
            System.out.println(isAuthorized);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
