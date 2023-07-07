package com.afcas.commands;

import com.afcas.factory.AuthorizationManagerFactory;
import com.afcas.objects.IAuthorizationManager;
import com.afcas.objects.Resource;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;

@Command(
        name = "resource",
        description = "add / update an resource"
)
public class AddResource implements Runnable {

    @Parameters(index = "0", description = "The resource name to add / update", arity = "1")
    private String id;

    @Parameters(index = "1", description = "The display name of the resource", arity = "0..1")
    private String name = "";

    @Override
    public void run() {
        try {
            if (id == null || id.isEmpty()) {
                System.out.println("Resource id must be provided!");
            }
            IAuthorizationManager authorizationManager = AuthorizationManagerFactory.getInstance();
            Resource resource = Resource.builder()
                    .id(id)
                    .name(name)
                    .build();
            authorizationManager.addOrUpdate(resource);
            System.out.println("Added Resource \"" + id + "\" successfully!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}

