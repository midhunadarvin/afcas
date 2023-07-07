package com.afcas.commands;

import com.afcas.factory.AuthorizationManagerFactory;
import com.afcas.objects.IAuthorizationManager;
import com.afcas.objects.Principal;
import com.afcas.objects.PrincipalType;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;


@Command(
        name = "group-member",
        description = "adds a group member"
)
public class AddGroupMember implements Runnable {

    @Parameters(index = "0", description = "The group name", arity = "1")
    private String groupName;

    @Parameters(index = "1", description = "The member name", arity = "1")
    private String memberName;

    @Override
    public void run() {
        try {
            IAuthorizationManager authorizationManager = AuthorizationManagerFactory.getInstance();
            /* TODO: Add check if it is a valid group */
            Principal group = Principal.builder()
                    .id(groupName)
                    .principalType(PrincipalType.Group)
                    .build();

            Principal member = Principal.builder()
                    .id(memberName)
                    .build();

            authorizationManager.addGroupMember(group, member);
            System.out.println("Added member \"" + memberName + "\" to group \"" + groupName + "\" successfully!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
