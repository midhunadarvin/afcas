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
        description = "adds a permission / access-predicate"
)
public class AddAccessPredicate implements Runnable {

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
        ResourceAccessPredicateType resourceAccessPredicateType;
        try {
            resourceAccessPredicateType = ResourceAccessPredicateType.valueOf(predicateType);
        } catch (Exception e) {
            System.out.println("Must be a valid PredicateType : Grant, Deny");
            return;
        }

        if( resourceAccessPredicateType != ResourceAccessPredicateType.Grant ) {
            throw new IllegalArgumentException( "Explicit denials are not supported!");
        }

        try {
            IAuthorizationManager authorizationManager = AuthorizationManagerFactory.getInstance();
            ResourceAccessPredicate resourceAccessPredicate = ResourceAccessPredicate.builder()
                    .principalId(principalName)
                    .operationId(operationName)
                    .resourceId(resourceName)
                    .accessPredicateType(resourceAccessPredicateType)
                    .build();
            authorizationManager.addAccessPredicate(resourceAccessPredicate);
            System.out.println("Added permission successfully!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
