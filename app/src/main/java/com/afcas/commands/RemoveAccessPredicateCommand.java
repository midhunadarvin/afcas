package com.afcas.commands;

import com.afcas.factory.AuthorizationManagerFactory;
import com.afcas.objects.IAuthorizationManager;
import com.afcas.objects.ResourceAccessPredicate;
import com.afcas.objects.ResourceAccessPredicateType;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;

@Command(
        name = "access-predicate",
        aliases = {"permission"},
        description = "removes a permission / access predicate"
)
public class RemoveAccessPredicateCommand implements Runnable {

    @Parameters(index = "0", description = "The principal name", arity = "1")
    private String principalName;

    @Parameters(index = "1", description = "The operation name", arity = "1")
    private String operationName;

    @Parameters(index = "2", description = "The resource name", arity = "1")
    private String resourceName;

    @Parameters(index = "3", description = "Grant / Deny", arity = "1")
    private String predicateType;

    @Override
    public void run() {
        ResourceAccessPredicateType pType;
        try {
            pType = ResourceAccessPredicateType.valueOf(predicateType);
        } catch (Exception e) {
            System.out.println("Must be a valid PredicateType : Grant, Deny");
            return;
        }

        try {
            IAuthorizationManager authorizationManager = AuthorizationManagerFactory.getInstance();
            ResourceAccessPredicate resourceAccessPredicate = ResourceAccessPredicate.builder()
                    .principalId(principalName)
                    .operationId(operationName)
                    .resourceId(resourceName)
                    .accessPredicateType(pType)
                    .build();
            authorizationManager.removeAccessPredicate(resourceAccessPredicate);
            System.out.println("Removed permission successfully!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
