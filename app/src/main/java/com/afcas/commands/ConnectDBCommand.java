package com.afcas.commands;

import com.afcas.InteractiveParameterConsumer;
import com.afcas.utils.DatabaseHelper;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.util.Arrays;

@Command(
        name = "connect-db",
        description = "Connects to a database"
)
public class ConnectDBCommand implements Runnable {

    @Option(names = {"-h", "--host"}, paramLabel = "HOSTNAME", description = "database host name", required = true)
    String hostname;

    @Option(names = {"-p", "--port"}, paramLabel = "PORT", description = "database port")
    String port;

    @Option(names = {"-U", "--username"}, paramLabel = "USERNAME", description = "database user name", required = true)
    String username = "";

    @Option(names = {"-W", "--password"}, paramLabel = "PASSWORD", description = "prompt user password", parameterConsumer = InteractiveParameterConsumer.class)
    String password;

    @Override
    public void run() {
        try {
            if (password == null && System.console() != null) {
                // alternatively, use Console::readPassword
                password = new String(System.console().readPassword("Enter value for --password: "));
            }
            DatabaseHelper.init(hostname, port, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
