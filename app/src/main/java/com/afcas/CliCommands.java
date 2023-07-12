package com.afcas;

import com.afcas.commands.Add;
import com.afcas.commands.ConnectDB;
import com.afcas.commands.Get;
import com.afcas.commands.IsAuthorized;
import com.afcas.commands.IsMemberOf;
import com.afcas.commands.IsSubOperation;
import com.afcas.commands.IsSubResource;
import com.afcas.commands.Remove;
import com.afcas.utils.DatabaseHelper;
import jline.console.ConsoleReader;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Spec;
import java.io.PrintWriter;

import static com.afcas.AfcasApp.description;

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
                AfcasApp.ClearScreen.class,
                IsAuthorized.class,
                IsMemberOf.class,
                IsSubOperation.class,
                IsSubResource.class
        })
public class CliCommands implements Runnable {
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

    public CliCommands(ConsoleReader reader) {
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
