package com.afcas.commands;

import com.afcas.factory.AuthorizationManagerFactory;
import com.afcas.objects.IAuthorizationManager;
import com.afcas.objects.Operation;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;


@Command(
        name = "sub-operation",
        description = "adds a sub operation"
)
public class AddSubOperation implements Runnable {

    @Parameters(index = "0", description = "The parent operation name", arity = "1")
    private String parentOperationName;

    @Parameters(index = "1", description = "The sub operation name", arity = "1")
    private String subOperationName;

    @Override
    public void run() {
        try {
            IAuthorizationManager authorizationManager = AuthorizationManagerFactory.getInstance();
            /* TODO: Add check if it is a valid operation */
            Operation subOperation = Operation.builder()
                    .id(subOperationName)
                    .build();

            Operation parentOperation = Operation.builder()
                    .id(parentOperationName)
                    .build();

            authorizationManager.addSubOperation(parentOperation, subOperation);
            System.out.println("Added sub operation \"" + subOperationName + "\" to parent operation \"" + parentOperationName + "\" successfully!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
