package fhirut;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Option;
import fhirut.core.FhirutRunner;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

@Command(name = "fut", mixinStandardHelpOptions = true, version = "fut 1.0",
        description = "FHIR Unit Test Tool")
public class FhirutCommand implements Callable<Integer> {

    @Parameters(index = "0..*", description = "Test files or patterns to run")
    private List<File> testFiles;

    @Option(names = {"-o", "--output"}, description = "Output directory for reports")
    private File outputDir;

    @Option(names = {"-v", "--verbose"}, description = "Verbose output")
    private boolean verbose;

    @Override
    public Integer call() throws Exception {
        FhirutRunner runner = new FhirutRunner();

        if (testFiles == null || testFiles.isEmpty()) {
            // Run all tests in current directory
            runner.runAllTests(new File("."));
        } else {
            // Run specified tests
            runner.runTests(testFiles);
        }

        return 0;
    }
}