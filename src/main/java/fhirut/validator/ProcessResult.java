package fhirut.validator;

/**
 * Encapsula o resultado da execução do validador FHIR externo
 */
public class ProcessResult {
    private final String output;
    private final String errors;
    private final int exitCode;

    public ProcessResult(String output, String errors, int exitCode) {
        this.output = output;
        this.errors = errors;
        this.exitCode = exitCode;
    }

    public String getOutput() {
        return output;
    }

    public String getErrors() {
        return errors;
    }

    public int getExitCode() {
        return exitCode;
    }

    public boolean isSuccess() {
        return exitCode == 0;
    }
}