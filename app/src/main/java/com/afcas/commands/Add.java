package com.afcas.commands;

import picocli.CommandLine.Command;

@Command(
        name = "add",
        description = "adds a entity ( principal / operation / resource )",
        subcommands = {
                AddPrincipal.class,
                AddOperation.class,
                AddResource.class,
                AddAccessPredicate.class,
                AddGroupMember.class
        }
)
public class Add implements Runnable {

    @Override
    public void run() {
        System.out.println("Subcommand needed: 'principal', 'operation' or 'resource'");
    }
}

