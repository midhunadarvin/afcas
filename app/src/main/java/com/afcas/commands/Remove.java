package com.afcas.commands;

import picocli.CommandLine.Command;

@Command(
        name = "remove",
        aliases = {"rm"},
        description = "removes a entity ( principal / operation / resource )",
        subcommands = {
                RemovePrincipal.class,
                RemoveOperation.class,
                RemoveResource.class,
                RemoveAccessPredicate.class,
                RemoveSubOperation.class,
                RemoveSubResource.class
        }
)
public class Remove implements Runnable {

    @Override
    public void run() {
        System.out.println("Subcommand needed: 'principal', 'operation' or 'resource'");
    }
}


