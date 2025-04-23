package fhirut.validator;

import fhirut.exceptions.FhirValidationException;
import fhirut.model.*;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.formats.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Classe responsável por validar recursos FHIR usando o validador oficial do HL7.
 */
public class FhirValidator {
    private static final String VALIDATOR_JAR = "lib/validator_cli.jar";
    private static final String FHIR_VERSION = "4.0.1";

    /**
     * Executa a validação de um teste FHIR baseado em uma definição.
     *
     * @param testDefinition a definição do teste, incluindo o recurso, contexto e resultados esperados.
     * @return resultado do teste com o OperationOutcome e status da validação.
     * @throws FhirValidationException se ocorrer algum erro na execução ou no processamento.
     */
    public TestResult validate(TestDefinition testDefinition) throws FhirValidationException {
        Objects.requireNonNull(testDefinition, "TestDefinition não pode ser nulo");

        try {
            List<String> command = buildCommand(testDefinition);
            ProcessResult result = executeCommand(command);
            return processResult(testDefinition, result.getOutput());
        } catch (IOException | InterruptedException e) {
            throw new FhirValidationException("Falha na execução do validador", e);
        }
    }

    /**
     * Constrói o comando de execução para o validador baseado na definição do teste.
     *
     * @param definition a definição do teste.
     * @return lista de argumentos para o comando.
     * @throws FhirValidationException se o recurso principal não for válido.
     */
    private List<String> buildCommand(TestDefinition definition) throws FhirValidationException {
        List<String> command = new ArrayList<>();
        command.add("java");
        command.add("-jar");
        command.add(VALIDATOR_JAR);
        command.add("-version");
        command.add(FHIR_VERSION);

        if (definition.getContext() != null) {
            TestContext context = definition.getContext();

            if (context.getIgs() != null) {
                for (String ig : context.getIgs()) {
                    command.add("-ig");
                    command.add(ig);
                }
            }

            if (context.getProfiles() != null) {
                for (String profile : context.getProfiles()) {
                    command.add("-profile");
                    command.add(profile);
                }
            }

            if (context.getResources() != null) {
                for (File resource : context.getResources()) {
                    if (resource.exists()) {
                        command.add(resource.getAbsolutePath());
                    }
                }
            }
        }

        File instanceFile = definition.getInstancePath();
        if (instanceFile == null || !instanceFile.exists()) {
            throw new FhirValidationException("Arquivo de instância inválido: " +
                    (instanceFile != null ? instanceFile.getPath() : "null"));
        }
        command.add(instanceFile.getAbsolutePath());

        return command;
    }

    /**
     * Executa o comando externo do validador e captura sua saída.
     *
     * @param command o comando completo a ser executado.
     * @return objeto contendo a saída e o código de retorno do processo.
     * @throws IOException se ocorrer erro de I/O.
     * @throws InterruptedException se o processo for interrompido.
     */
    private ProcessResult executeCommand(List<String> command) throws IOException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.redirectErrorStream(true);

        Process process = builder.start();

        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }

        return new ProcessResult(output.toString(), process.waitFor());
    }

    /**
     * Processa a saída JSON do validador, criando um {@link TestResult}.
     *
     * @param definition definição do teste original.
     * @param jsonOutput saída JSON do validador.
     * @return objeto {@link TestResult} com o resultado e status de aprovação.
     * @throws FhirValidationException se falhar ao interpretar o JSON.
     */
    private TestResult processResult(TestDefinition definition, String jsonOutput)
            throws FhirValidationException {
        try {
            OperationOutcome outcome = (OperationOutcome) new JsonParser().parse(jsonOutput);
            TestResult result = new TestResult();
            result.setTestId(definition.getTestId());
            result.setOutcome(outcome);

            if (definition.getExpectedResults() != null) {
                boolean validationPassed = isValidationSuccessful(outcome, definition.getExpectedResults());
                result.setPassed(validationPassed);
            }

            return result;
        } catch (Exception e) {
            throw new FhirValidationException("Falha ao processar resultado", e);
        }
    }

    /**
     * Compara o resultado obtido com o esperado.
     *
     * @param outcome resultado retornado pelo validador.
     * @param expected resultados esperados conforme definição do teste.
     * @return {@code true} se o resultado está conforme o esperado, {@code false} caso contrário.
     */
    private boolean isValidationSuccessful(OperationOutcome outcome, ExpectedResults expected) {
        boolean hasErrors = outcome.getIssue().stream()
                .anyMatch(issue -> issue.getSeverity() == OperationOutcome.IssueSeverity.ERROR);

        boolean expectedSuccess = "success".equalsIgnoreCase(expected.getStatus());

        return hasErrors != expectedSuccess; // XOR lógico
    }
}
