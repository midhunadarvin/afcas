package com.afcas.commands;

import picocli.CommandLine.Command;

@Command(
        name = "add",
        description = "adds a entity ( principal / operation / resource )",
        subcommands = {
                AddPrincipalCommand.class, AddOperationCommand.class, AddResourceCommand.class, AddAccessPredicateCommand.class
        }
)
public class AddCommand implements Runnable {

    @Override
    public void run() {
        System.out.println("Subcommand needed: 'principal', 'operation' or 'resource'");
    }
}

