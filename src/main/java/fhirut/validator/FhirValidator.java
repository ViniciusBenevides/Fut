package fhirut.validator;

import fhirut.exceptions.FhirValidationException;
import fhirut.model.TestDefinition;
import fhirut.model.TestResult;
import fhirut.validator.ProcessResult;
import org.hl7.fhir.r4.model.OperationOutcome;

import java.util.List;

public class FhirValidator {
    private static final String VALIDATOR_JAR = "lib/validator_cli.jar";

    public TestResult validate(TestDefinition testDefinition) throws FhirValidationException {
        // 1. Preparar comando para executar o validador
        List<String> command = buildValidatorCommand(testDefinition);

        // 2. Executar o processo
        ProcessResult processResult = executeValidatorProcess(command);

        // 3. Parsear o OperationOutcome resultante
        OperationOutcome outcome = parseOperationOutcome(processResult.getOutput());

        // 4. Comparar com os resultados esperados
        return compareResults(testDefinition, outcome);
    }

    private List<String> buildValidatorCommand(TestDefinition definition) {
        // Implementar construção do comando Java para executar o validador
    }

    private ProcessResult executeValidatorProcess(List<String> command) throws FhirValidationException {
        // Implementar execução do processo externo
    }

    private OperationOutcome parseOperationOutcome(String json) {
        // Implementar parse do JSON para OperationOutcome
    }

    private TestResult compareResults(TestDefinition definition, OperationOutcome outcome) {
        // Implementar comparação entre resultados esperados e obtidos
    }
}