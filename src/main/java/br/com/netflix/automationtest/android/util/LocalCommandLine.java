package br.com.netflix.automationtest.android.util;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Logger;

public class LocalCommandLine {

    final ArrayList<String> commands = new ArrayList<>();
    private static final Logger log = Logger.getLogger(LocalCommandLine.class.getName());

    public void executeCommand(final String command) throws IOException {

        if (isWindows()) {
            commands.add("cmd.exe");
            commands.add("/c");
        } else {
            commands.add("/bin/bash");
            commands.add("-c");
        }

        commands.add(command);
        BufferedReader br = null;

        try {
            final ProcessBuilder p = new ProcessBuilder(commands);
            final Process process = p.start();
            final InputStream is = process.getInputStream();
            final InputStreamReader isr = new InputStreamReader(is);
            br = new BufferedReader(isr);

            System.out.println("Command Line Execution = [" + command + "]");

            String line;
            while ((line = br.readLine()) != null) {
                System.out.println("Command return = [" + line + "]");
            }

        } catch (IOException ioe) {
            log.severe("Error Executing Shell Command: " + ioe.getMessage());
            throw ioe;
        } finally {
            secureClose(br);
        }
    }

    private void secureClose(final Closeable resource) {
        try {
            if (resource != null) {
                resource.close();
            }
        } catch (IOException ex) {
            log.severe("Error = " + ex.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        final LocalCommandLine shell = new LocalCommandLine();
        shell.executeCommand("ping -c 3 8.8.8.8"); // -c 3 para limitar a 3 pacotes (Unix)
    }

    // OS Checkers
    public static boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("win");
    }

    public static boolean isMac() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("mac");
    }

    public static boolean isUnix() {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.contains("nix") || os.contains("nux"));
    }

    public static boolean isSolaris() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("sunos");
    }
}
