package com.afcas.commands;

import com.afcas.factory.AuthorizationManagerFactory;
import com.afcas.objects.IAuthorizationManager;
import com.afcas.objects.Operation;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;

@Command(
        name = "operation",
        description = "add / update an operation"
)
public class AddOperation implements Runnable {

    @Parameters(index = "0", description = "The operation name to add / update", arity = "1")
    private String id;

    @Parameters(index = "1", description = "The display name", arity = "0..1")
    private String name = "";

    @Parameters(index = "2", description = "The description of the operation", arity = "0..1")
    private String description = "";

    @Override
    public void run() {
        try {
            if (id == null || id.isEmpty()) {
                System.out.println("Operation id must be provided!");
            }
            IAuthorizationManager authorizationManager = AuthorizationManagerFactory.getInstance();
            Operation op = Operation.builder()
                    .id(id)
                    .name(name)
                    .description(description)
                    .build();
            authorizationManager.addOrUpdate(op);
            System.out.println("Added Operation \"" + id + "\" successfully!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}

