package com.afcas.commands;

import com.afcas.factory.AuthorizationManagerFactory;
import com.afcas.objects.IAuthorizationManager;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;

@Command(
        name = "operation",
        description = "removes a operation"
)
public class RemoveOperation implements Runnable {

    @Parameters(index = "0", description = "The Operation name to remove", arity = "1")
    private String name;

    @Override
    public void run() {
        if (name == null || name.isEmpty()) {
            System.out.println("Operation name must be provided!");
        }

        try {
            IAuthorizationManager authorizationManager = AuthorizationManagerFactory.getInstance();
            authorizationManager.removeOperation(name);
            System.out.println("Removed Operation \"" + name + "\" successfully!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
