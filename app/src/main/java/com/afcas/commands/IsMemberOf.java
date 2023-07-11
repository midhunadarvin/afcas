package com.afcas.commands;

import com.afcas.impl.AuthorizationProvider;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(
        name = "is-member-of",
        description = "Check if the specified entity is member of a group"
)
public class IsMemberOf implements Runnable {

    private AuthorizationProvider authorizationProvider;

    @Parameters(index = "0", description = "The Principal name", arity = "1")
    private String groupId;

    @Parameters(index = "1", description = "The operation name", arity = "1")
    private String memberId;

    public IsMemberOf() {
        this.authorizationProvider = new AuthorizationProvider();
    }

    @Override
    public void run() {
        try {
            Boolean isMemberOf = authorizationProvider.isMemberOf(groupId, memberId);
            System.out.println(isMemberOf);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
