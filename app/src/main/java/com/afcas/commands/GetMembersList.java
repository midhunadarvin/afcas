package com.afcas.commands;

import com.afcas.factory.AuthorizationManagerFactory;
import com.afcas.objects.IAuthorizationManager;
import com.afcas.objects.Principal;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;

import java.util.Objects;

@Command(
        name = "members",
        description = "gets the list of members of a group"
)
public class GetMembersList implements Runnable {

    @Parameters(index = "0", description = "The group name", arity = "1")
    private String groupName;

    @Parameters(index = "1", description = "The isFlat flag", arity = "0..1")
    private String isFlat;

    @Override
    public void run() {

        try {
            IAuthorizationManager authorizationManager = AuthorizationManagerFactory.getInstance();
            Principal pr = Principal.builder()
                    .id(groupName)
                    .build();
            if (Objects.equals(isFlat, "true")) {
                authorizationManager.getFlatMembersList(pr);
            } else {
                authorizationManager.getMembersList(pr);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
