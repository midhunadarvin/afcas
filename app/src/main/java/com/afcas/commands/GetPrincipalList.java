package com.afcas.commands;

import com.afcas.factory.AuthorizationManagerFactory;
import com.afcas.objects.IAuthorizationManager;
import com.afcas.objects.Principal;
import com.afcas.objects.PrincipalType;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;

import java.util.List;

@Command(
        name = "principal",
        description = "gets the list of principals"
)
public class GetPrincipalList implements Runnable {

    @Parameters(index = "0", description = "The principal type", arity = "0..1")
    private String principalType;

    @Override
    public void run() {

        PrincipalType pType = null;
        if (principalType != null && !principalType.isEmpty()) {
            try {
                pType = PrincipalType.valueOf(principalType);
            } catch (Exception e) {
                System.out.println("Must be a valid PrincipalType : User, Group");
                return;
            }
        }

        try {
            IAuthorizationManager authorizationManager = AuthorizationManagerFactory.getInstance();
            if (pType != null) {
                authorizationManager.getPrincipalList(pType);
            } else {
                authorizationManager.getPrincipalList();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
