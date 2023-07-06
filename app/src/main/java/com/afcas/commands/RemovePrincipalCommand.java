package com.afcas.commands;

import com.afcas.factory.AuthorizationManagerFactory;
import com.afcas.objects.IAuthorizationManager;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;

@Command(
        name = "principal",
        description = "removes a principal"
)
public class RemovePrincipalCommand implements Runnable {

    @Parameters(index = "0", description = "The principal name to remove", arity = "1")
    private String name;

    @Override
    public void run() {
        if (name == null || name.isEmpty()) {
            System.out.println("Principal name must be provided!");
        }

        try {
            IAuthorizationManager authorizationManager = AuthorizationManagerFactory.getInstance();
            authorizationManager.removePrincipal(name);
            System.out.println("Removed Principal \"" + name + "\" successfully!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
