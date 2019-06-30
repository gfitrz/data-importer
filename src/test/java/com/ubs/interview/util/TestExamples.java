package com.ubs.interview.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@AllArgsConstructor
public enum TestExamples {
    IMPORT_FILE_CORRECT_VALUES("import_file_correct_values.txt", MediaType.TEXT_PLAIN),
    IMPORT_FILE_INCORRECT_VALUES("import_file_incorrect_values.txt", MediaType.TEXT_PLAIN),
    IMPORT_FILE_DUPLICATED_VALUES("import_file_duplicated_values.txt", MediaType.TEXT_PLAIN),
    IMPORTED_DATA_RESPONSE("imported_data_response.json"),
    IMPORTED_DATA_RESPONSE_ARRAY("imported_data_response_array.json");

    private final String messageName;

    @Getter
    private final MediaType mediaType;

    TestExamples(String messageName) {
        this(messageName, MediaType.APPLICATION_JSON_UTF8);
    }

    public String getBody() {
        return new String(getBytes(), StandardCharsets.UTF_8);
    }

    public byte[] getBytes() {
        try {
            return Files.readAllBytes(getResourcePath());
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public InputStream getInputStream() {
        try {
            return Files.newInputStream(getResourcePath());
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private Path getResourcePath() throws URISyntaxException {
        URI resourceUri = TestExamples.class.getResource(String.format("/examples/%s", messageName)).toURI();
        return Paths.get(resourceUri);
    }
}