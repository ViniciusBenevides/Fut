package fhirut;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Option;
import fhirut.core.FhirutRunner;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Classe principal de comando da aplicação fut (FHIR Unit Test Tool).
 * Utiliza a biblioteca picocli para definir comandos de terminal.
 */
@Command(
        name = "fut",                           // Nome do comando no terminal
        mixinStandardHelpOptions = true,       // Adiciona automaticamente opções --help e --version
        version = "fut 1.0",                   // Versão do programa
        description = "FHIR Unit Test Tool"    // Descrição exibida no help
)

public class FhirutCommand implements Callable<Integer> {

    /**
     * Arquivos de teste ou padrões passados na linha de comando.
     * Ex: teste1.yaml teste2.yaml
     */
    @Parameters(index = "0..*", description = "Test files or patterns to run")
    private List<File> testFiles;

    /**
     * Diretório de saída para os relatórios gerados.
     * Ex: -o resultados/
     */
    @Option(names = {"-o", "--output"}, description = "Output directory for reports")
    private File outputDir;

    /**
     * Habilita modo detalhado (verbose).
     * Ex: -v
     */
    @Option(names = {"-v", "--verbose"}, description = "Verbose output")
    private boolean verbose;

    /**
     * Método principal executado quando o comando é chamado.
     * Responsável por iniciar a execução dos testes.
     */
    @Override
    public Integer call() throws Exception {
        FhirutRunner runner = new FhirutRunner();

        // Se nenhum arquivo for passado, executa todos os testes do diretório atual
        if (testFiles == null || testFiles.isEmpty()) {
            runner.runAllTests(new File("."));
        } else {
            // Executa apenas os testes especificados
            runner.runTests(testFiles);
        }

        return 0; // Código de saída 0 = sucesso
    }
}
