package com.afcas.commands;

import picocli.CommandLine.Command;

@Command(
        name = "get",
        description = "gets a entity ( principal / operation / resource )",
        subcommands = {
                GetPrincipalList.class,
                GetMembersList.class
        }
)
public class Get implements Runnable {

    @Override
    public void run() {
        System.out.println("Subcommand needed: 'principal', 'operation' or 'resource'");
    }
}

