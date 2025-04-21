package fhirut.model;

import java.io.File;
import java.util.List;

public class TestDefinition {
    private String testId;
    private String description;
    private TestContext context;
    private File instancePath;
    private ExpectedResults expectedResults;

    // Getters e Setters
}

class TestContext {
    private List<String> igs;
    private List<String> profiles;
    private List<File> resources;

    // Getters e Setters
}

class ExpectedResults {
    private String status;
    private List<String> errors;
    private List<String> warnings;
    private List<String> informations;
    private List<InvariantCheck> invariants;

    // Getters e Setters
}

class InvariantCheck {
    private String expression;
    private boolean expected;

    // Getters e Setters
}