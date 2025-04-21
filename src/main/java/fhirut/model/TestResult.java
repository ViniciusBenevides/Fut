package fhirut.model;

import org.hl7.fhir.r4.model.OperationOutcome;

import java.util.List;

public class TestResult {
    private String testId;
    private boolean passed;
    private OperationOutcome outcome;
    private List<ValidationDiff> differences;

    // Getters e Setters
}

class ValidationDiff {
    private String type; // "MISSING_ERROR", "UNEXPECTED_ERROR", etc.
    private String message;
    private String location;
    private String severity;

    // Getters e Setters
}
