package fhirut;

import picocli.CommandLine;

public class Main {
    public static void main(String[] args) {
        int exitCode = new CommandLine(new FhirutCommand()).execute(args);
        System.exit(exitCode);
    }
}