package com.afcas.commands;

import com.afcas.factory.AuthorizationManagerFactory;
import com.afcas.objects.IAuthorizationManager;
import com.afcas.objects.Resource;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;


@Command(
        name = "sub-resource",
        description = "adds a sub resource"
)
public class AddSubResource implements Runnable {

    @Parameters(index = "0", description = "The parent resource name", arity = "1")
    private String parentResourceName;

    @Parameters(index = "1", description = "The sub resource name", arity = "1")
    private String subResourceName;

    @Override
    public void run() {
        try {
            IAuthorizationManager authorizationManager = AuthorizationManagerFactory.getInstance();
            /* TODO: Add check if it is a valid resource */
            Resource subResource = Resource.builder()
                    .id(subResourceName)
                    .build();

            Resource parentResource = Resource.builder()
                    .id(parentResourceName)
                    .build();

            authorizationManager.addSubResource(parentResource, subResource);
            System.out.println("Added sub resource \"" + subResourceName + "\" to parent resource \"" + parentResourceName + "\" successfully!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
