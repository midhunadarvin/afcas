package com.afcas.commands;

import com.afcas.impl.AuthorizationProvider;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(
        name = "is-sub-operation",
        description = "Check if the specified entity is sub-operation of an operation"
)
public class IsSubOperation implements Runnable {

    private AuthorizationProvider authorizationProvider;

    @Parameters(index = "0", description = "The Principal name", arity = "1")
    private String operationId;

    @Parameters(index = "1", description = "The operation name", arity = "1")
    private String subOperationId;

    public IsSubOperation() {
        this.authorizationProvider = new AuthorizationProvider();
    }

    @Override
    public void run() {
        try {
            Boolean isMemberOf = authorizationProvider.isSubOperation(operationId, subOperationId);
            System.out.println(isMemberOf);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

