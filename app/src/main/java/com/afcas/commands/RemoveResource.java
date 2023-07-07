package com.afcas.commands;

import com.afcas.factory.AuthorizationManagerFactory;
import com.afcas.objects.IAuthorizationManager;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;

@Command(
        name = "resource",
        description = "removes a resource"
)
public class RemoveResource implements Runnable {

    @Parameters(index = "0", description = "The Resource name to remove", arity = "1")
    private String name;

    @Override
    public void run() {
        if (name == null || name.isEmpty()) {
            System.out.println("Resource name must be provided!");
        }

        try {
            IAuthorizationManager authorizationManager = AuthorizationManagerFactory.getInstance();
            authorizationManager.removeResource(name);
            System.out.println("Removed Resource \"" + name + "\" successfully!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
