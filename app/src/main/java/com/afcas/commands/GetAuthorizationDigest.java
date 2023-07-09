package com.afcas.commands;

import com.afcas.impl.AuthorizationProvider;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;

@Command(
        name = "authorization-digest",
        description = "gets the authorization digest"
)
public class GetAuthorizationDigest implements Runnable {

    private AuthorizationProvider authorizationProvider;

    @Parameters(index = "0", description = "The principal name", arity = "1")
    private String principalName;

    public GetAuthorizationDigest() {
        this.authorizationProvider = new AuthorizationProvider(3600);
    }

    @Override
    public void run() {

        try {
            authorizationProvider.getAuthorizationDigest(principalName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
