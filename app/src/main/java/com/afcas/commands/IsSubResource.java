package com.afcas.commands;

import com.afcas.impl.AuthorizationProvider;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(
        name = "is-sub-resource",
        description = "Check if the specified entity is sub-resource of a resource"
)
public class IsSubResource implements Runnable {

    private AuthorizationProvider authorizationProvider;

    @Parameters(index = "0", description = "The resource name", arity = "1")
    private String resourceId;

    @Parameters(index = "1", description = "The sub resource name", arity = "1")
    private String subResourceId;

    public IsSubResource() {
        this.authorizationProvider = new AuthorizationProvider(3600);
    }

    @Override
    public void run() {
        try {
            Boolean isSubResource = authorizationProvider.isSubResource(resourceId, subResourceId);
            System.out.println(isSubResource);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

