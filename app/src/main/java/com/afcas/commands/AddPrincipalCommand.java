package com.afcas.commands;

import com.afcas.factory.AuthorizationManagerFactory;
import com.afcas.objects.IAuthorizationManager;
import com.afcas.objects.Principal;
import com.afcas.objects.PrincipalType;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;

@Command(
        name = "principal",
        description = "adds a principal"
)
public class AddPrincipalCommand implements Runnable {

    @Parameters(index = "0", description = "The principal name to add / update", arity = "1")
    private String name;

    @Parameters(index = "1", description = "The principal type", arity = "1")
    private String principalType = "";

    @Parameters(index = "2", description = "The display name", arity = "0..1")
    private String displayName = "";

    @Parameters(index = "3", description = "The email of the principal", arity = "0..1")
    private String email = "";

    @Parameters(index = "4", description = "The description of the principal", arity = "0..1")
    private String description = "";

    @Parameters(index = "5", description = "The principal source", arity = "0..1")
    private String source = "";

    @Override
    public void run() {
        if (name == null || name.isEmpty()) {
            System.out.println("Principal name must be provided!");
        }

        try {
            IAuthorizationManager authorizationManager = AuthorizationManagerFactory.getInstance();
            Principal pr = Principal.builder()
                    .name(name)
                    .email(email)
                    .displayName(displayName)
                    .principalType(PrincipalType.valueOf(principalType))
                    .description(description)
                    .dataSource(source)
                    .build();
            authorizationManager.addOrUpdate(pr, source);
            System.out.println("Added Principal \"" + name + "\" successfully!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
