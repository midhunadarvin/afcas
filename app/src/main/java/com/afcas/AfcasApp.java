/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.afcas;

import jline.TerminalFactory;
import jline.internal.Configuration;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.IOException;
import java.util.concurrent.Callable;

import jline.console.ConsoleReader;
import jline.console.completer.ArgumentCompleter.ArgumentList;
import jline.console.completer.ArgumentCompleter.WhitespaceArgumentDelimiter;
import picocli.CommandLine.Help;
import picocli.CommandLine.IFactory;
import picocli.CommandLine.ParentCommand;
import picocli.shell.jline2.PicocliJLineCompleter;

public class AfcasApp {

    final static String description = "\nWelcome to our new Command Line Interface (CLI) tool designed to" +
            " streamline and simplify the process of authorization. This CLI tool aims to provide" +
            " developers and administrators with a powerful and efficient solution for managing authorization " +
            " rules and policies within our system.";




    /**
     * Command that clears the screen.
     */
    @Command(name = "cls", aliases = "clear", mixinStandardHelpOptions = true,
            description = "Clears the screen", version = "1.0")
    static class ClearScreen implements Callable<Void> {
        @ParentCommand
        CliCommands parent;

        public Void call() throws IOException {
            parent.reader.clearScreen();
            return null;
        }
    }

    public static void main(String[] args) {
        // JLine 2 does not detect some terminal as not ANSI compatible (e.g  Eclipse Console)
        // See : https://github.com/jline/jline2/issues/185
        // This is an optional workaround which allow to use picocli heuristic instead :
        if (!Help.Ansi.AUTO.enabled() && //
                Configuration.getString(TerminalFactory.JLINE_TERMINAL, TerminalFactory.AUTO)
                        .equalsIgnoreCase(TerminalFactory.AUTO)) {
            TerminalFactory.configure(TerminalFactory.Type.NONE);
        }

        try {
            ConsoleReader reader = new ConsoleReader();
            IFactory factory = new CustomFactory(new InteractiveParameterConsumer(reader));

            // set up the completion
            CliCommands commands = new CliCommands(reader);
            CommandLine cmd = new CommandLine(commands, factory);
            reader.addCompleter(new PicocliJLineCompleter(cmd.getCommandSpec()));

            cmd.execute(args);

            // start the shell and process input until the user quits with Ctrl-C
            String line;
            while ((line = reader.readLine("afcas> ")) != null) {
                ArgumentList list = new WhitespaceArgumentDelimiter()
                        .delimit(line, line.length());
                new CommandLine(commands, factory)
                        .execute(list.getArguments());
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}