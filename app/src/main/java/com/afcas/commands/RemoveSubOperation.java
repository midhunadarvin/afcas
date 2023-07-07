package com.afcas.commands;

import com.afcas.factory.AuthorizationManagerFactory;
import com.afcas.objects.IAuthorizationManager;
import com.afcas.objects.Operation;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;


@Command(
        name = "sub-operation",
        description = "removes a sub operation"
)
public class RemoveSubOperation implements Runnable {

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

            authorizationManager.removeSubOperation(parentOperation, subOperation);
            System.out.println("Removed sub operation \"" + subOperationName + "\" from parent operation \"" + parentOperationName + "\" successfully!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
