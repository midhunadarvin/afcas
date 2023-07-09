package com.afcas.commands;

import com.afcas.factory.AuthorizationManagerFactory;
import com.afcas.objects.IAuthorizationManager;
import com.afcas.objects.Operation;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;


@Command(
        name = "operations",
        description = "gets the list of operations"
)
public class GetOperationList implements Runnable {

    @Parameters(index = "0", description = "The parent operation name", arity = "0..1")
    private String parentOperation;

    @Parameters(index = "1", description = "The isFlat flag", arity = "0..1")
    private String isFlat;

    @Override
    public void run() {

        try {
            IAuthorizationManager authorizationManager = AuthorizationManagerFactory.getInstance();

            if (parentOperation != null && !parentOperation.isEmpty()) {
                Operation op = Operation.builder()
                        .id(parentOperation)
                        .build();
                if (isFlat != null && isFlat == "true") {
                    authorizationManager.getFlatSubOperationsList(op);
                } else {
                    authorizationManager.getSubOperationsList(op);
                }
            } else {
                authorizationManager.getOperationList();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
