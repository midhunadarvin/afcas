/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.afcas;

import com.afcas.commands.Add;
import com.afcas.commands.Get;
import com.afcas.commands.Remove;
import com.afcas.commands.ConnectDB;
import com.afcas.commands.IsAuthorized;
import com.afcas.utils.DatabaseHelper;
import jline.TerminalFactory;
import jline.internal.Configuration;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.Callable;

import jline.console.ConsoleReader;
import jline.console.completer.ArgumentCompleter.ArgumentList;
import jline.console.completer.ArgumentCompleter.WhitespaceArgumentDelimiter;
import picocli.CommandLine.Help;
import picocli.CommandLine.IFactory;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;
import picocli.CommandLine.Spec;
import picocli.shell.jline2.PicocliJLineCompleter;

public class AfcasApp {

    final static String description = "\nWelcome to our new Command Line Interface (CLI) tool designed to" +
            " streamline and simplify the process of authorization. This CLI tool aims to provide" +
            " developers and administrators with a powerful and efficient solution for managing authorization " +
            " rules and policies within our system.";

    /**
     * Top-level command that just prints help.
     */
    @Command(
            name = "afcas",
            version = "AFCAS 1.0",
            description = {description},
            footer = {"", "Press Ctrl-C to exit."},
            mixinStandardHelpOptions = true,
            subcommands = {
                    Add.class,
                    Get.class,
                    Remove.class,
                    ConnectDB.class,
                    ClearScreen.class,
                    IsAuthorized.class
            })
    public static class CliCommands implements Runnable {
        public final ConsoleReader reader;
        final PrintWriter out;

        @Option(names = {"-h", "--host"}, paramLabel = "HOSTNAME", description = "database host name")
        String hostname;

        @Option(names = {"-p", "--port"}, paramLabel = "PORT", description = "database port")
        String port;

        @Option(names = {"-U", "--username"}, paramLabel = "USERNAME", description = "database user name")
        String username;

        @Option(names = {"-W", "--password"}, paramLabel = "PASSWORD", description = "prompt user password")
        String password;

        @Spec
        private CommandSpec spec;

        CliCommands(ConsoleReader reader) {
            this.reader = reader;
            out = new PrintWriter(reader.getOutput());
        }

        public void run() {
            try {
                if (!DatabaseHelper.isConnected()) {
                    if (hostname == null && System.console() != null) {
                        hostname = reader.readLine("Enter value for --hostname:  ");
                    }
                    if (port == null && System.console() != null) {
                        port = reader.readLine("Enter value for --port:  ");
                    }
                    if (username == null && System.console() != null) {
                        username = reader.readLine("Enter value for --username:  ");
                    }
                    if (password == null && System.console() != null) {
                        password = reader.readLine("Enter value for --password:  ", '*');
                    }
                    DatabaseHelper.init(hostname, port, username, password);
                } else {
                    // if the command was invoked without subcommand, show the usage help
                    out.println(spec.commandLine().getUsageMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

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
                Configuration.getString(TerminalFactory.JLINE_TERMINAL, TerminalFactory.AUTO).toLowerCase()
                        .equals(TerminalFactory.AUTO)) {
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