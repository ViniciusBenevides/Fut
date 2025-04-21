package fhirut.core;

import fhirut.exceptions.FhirutException;
import fhirut.model.TestDefinition;
import fhirut.model.TestResult;
import fhirut.parser.YamlParser;
import fhirut.validator.FhirValidator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FhirutRunner {
    private final FhirValidator validator;

    public FhirutRunner() {
        this.validator = new FhirValidator();
    }

    public void runAllTests(File directory) throws FhirutException {
        // Implementar lógica para encontrar todos os arquivos YAML no diretório
        // e executar os testes
    }

    public List<TestResult> runTests(List<File> testFiles) throws FhirutException {
        List<TestResult> results = new ArrayList<>();
        YamlParser parser = new YamlParser();

        for (File testFile : testFiles) {
            TestDefinition definition = parser.parseTestDefinition(testFile);
            TestResult result = validator.validate(definition);
            results.add(result);
        }

        return results;
    }
}