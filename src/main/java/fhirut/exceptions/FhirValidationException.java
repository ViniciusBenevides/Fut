package fhirut.exceptions;

import org.hl7.fhir.r4.model.OperationOutcome;

/**
 * Exceção lançada quando ocorrem erros durante a validação FHIR,
 * incluindo problemas com o validador externo ou resultados inesperados.
 */
public class FhirValidationException extends FhirutException {
    private OperationOutcome operationOutcome;

    /**
     * Constrói uma nova exceção com mensagem padrão.
     */
    public FhirValidationException() {
        super("Erro durante a validação FHIR");
    }

    /**
     * Constrói uma nova exceção com a mensagem especificada.
     * @param message a mensagem detalhando o erro de validação
     */
    public FhirValidationException(String message) {
        super(message);
    }

    /**
     * Constrói uma nova exceção com a causa especificada.
     * @param cause a causa raiz do erro de validação
     */
    public FhirValidationException(Throwable cause) {
        super(cause);
    }

    /**
     * Constrói uma nova exceção com a mensagem e causa especificadas.
     * @param message a mensagem detalhando o erro de validação
     * @param cause a causa raiz do erro de validação
     */
    public FhirValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constrói uma nova exceção com o OperationOutcome resultante da validação.
     * @param operationOutcome o resultado da validação FHIR
     */
    public FhirValidationException(OperationOutcome operationOutcome) {
        super("Falha na validação FHIR: " + operationOutcomeToString(operationOutcome));
        this.operationOutcome = operationOutcome;
    }

    /**
     * Constrói uma nova exceção com mensagem e OperationOutcome.
     * @param message a mensagem detalhando o erro
     * @param operationOutcome o resultado da validação FHIR
     */
    public FhirValidationException(String message, OperationOutcome operationOutcome) {
        super(message);
        this.operationOutcome = operationOutcome;
    }

    /**
     * Obtém o OperationOutcome associado à exceção.
     * @return o OperationOutcome ou null se não estiver disponível
     */
    public OperationOutcome getOperationOutcome() {
        return operationOutcome;
    }

    private static String operationOutcomeToString(OperationOutcome outcome) {
        if (outcome == null) {
            return "Sem detalhes de validação";
        }

        StringBuilder sb = new StringBuilder();
        if (outcome.hasIssue()) {
            outcome.getIssue().forEach(issue -> {
                sb.append("[")
                        .append(issue.getSeverity().getDisplay())
                        .append("] ")
                        .append(issue.getDetails().getText())
                        .append(" (")
                        .append(issue.getExpression().get(0).getValue())
                        .append(")\n");
            });
        }
        return sb.toString();
    }
}