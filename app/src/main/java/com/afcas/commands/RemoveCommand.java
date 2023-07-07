package com.afcas.commands;

import picocli.CommandLine.Command;

@Command(
        name = "remove",
        aliases = {"rm"},
        description = "removes a entity ( principal / operation / resource )",
        subcommands = {
                RemovePrincipalCommand.class,
                RemoveOperationCommand.class,
                RemoveResourceCommand.class,
                RemoveAccessPredicateCommand.class
        }
)
public class RemoveCommand implements Runnable {

    @Override
    public void run() {
        System.out.println("Subcommand needed: 'principal', 'operation' or 'resource'");
    }
}


