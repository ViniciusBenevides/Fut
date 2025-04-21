package fhirut.parser;

import fhirut.exceptions.FhirutParseException;
import fhirut.model.TestDefinition;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;

public class YamlParser {
    private final ObjectMapper yamlMapper;

    public YamlParser() {
        this.yamlMapper = new ObjectMapper(new YAMLFactory());
    }

    public TestDefinition parseTestDefinition(File yamlFile) throws FhirutParseException {
        try {
            return yamlMapper.readValue(yamlFile, TestDefinition.class);
        } catch (IOException e) {
            throw new FhirutParseException("Failed to parse YAML test definition", e);
        }
    }
}