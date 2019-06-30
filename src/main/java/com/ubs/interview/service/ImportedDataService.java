package com.ubs.interview.service;

import com.ubs.interview.db.entity.ImportedData;
import com.ubs.interview.db.repository.ImportedDataRepository;
import com.ubs.interview.rest.response.ImportedDataResponse;
import com.ubs.interview.service.dto.ImportedDataCsv;
import com.ubs.interview.service.exception.CsvReadException;
import com.ubs.interview.service.exception.DuplicateKeyException;
import com.ubs.interview.service.exception.ObjectNotFoundException;
import com.ubs.interview.service.mapper.ImportedDataMapper;
import lombok.AllArgsConstructor;
import org.csveed.api.CsvClient;
import org.csveed.api.CsvClientImpl;
import org.csveed.report.CsvException;
import org.mapstruct.factory.Mappers;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class ImportedDataService {

    private static final char CSV_DATA_SEPARATOR = ',';

    private final ImportedDataRepository importedDataRepository;
    private final ImportedDataMapper importedDataMapper;

    public List<ImportedDataResponse> getAll() {
        return StreamSupport.stream(importedDataRepository.findAll().spliterator(), false)
                .map(importedDataMapper::toImportedDataResponse)
                .collect(Collectors.toList());
    }

    public ImportedDataResponse get(String primaryKey) {
        return importedDataRepository.findById(primaryKey)
                .map(importedDataMapper::toImportedDataResponse)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Object with primaryKey %s not found.", primaryKey)));
    }

    public void delete(String primaryKey) {
        try {
            importedDataRepository.deleteById(primaryKey);
        } catch (EmptyResultDataAccessException ex) {
            throw new ObjectNotFoundException(String.format("Object with primaryKey [%s] not found.", primaryKey));
        }
    }

    public List<ImportedDataResponse> saveDataFromFile(InputStream fileInputStream) {
        List<ImportedDataCsv> importedData = readDataFromFile(fileInputStream);
        checkIfThereAreDuplicates(importedData);
        return saveCsvData(importedData).stream()
                .map(importedDataMapper::toImportedDataResponse)
                .collect(Collectors.toList());
    }

    List<ImportedDataCsv> readDataFromFile(InputStream fileInputStream) {
        try (InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8)) {
            CsvClient<ImportedDataCsv> csvReader = new CsvClientImpl<>(inputStreamReader, ImportedDataCsv.class);
            csvReader.setSeparator(CSV_DATA_SEPARATOR);

            return csvReader.readBeans();
        } catch (CsvException ex) {
            throw new CsvReadException(String.format("Incorrect line %d: %s", ex.getError().getLineNumber(), ex.getError().getMessage()));
        } catch (IOException ex) {
            throw new IllegalStateException("Something went wrong during reading file.");
        }
    }

    void checkIfThereAreDuplicates(List<ImportedDataCsv> importedData) {
        Set<String> duplicatedImportedData = importedData.stream()
                .filter(value -> Collections.frequency(importedData, value) > 1)
                .map(ImportedDataCsv::getPrimaryKey)
                .collect(Collectors.toSet());

        if (!duplicatedImportedData.isEmpty()) {
            throw new DuplicateKeyException(String.format("Primary keys: %s are duplicated in given data.", duplicatedImportedData));
        }
    }

    List<ImportedData> saveCsvData(List<ImportedDataCsv> importedDataCsv) {
        List<ImportedData> importedData = importedDataCsv.stream().map(importedDataMapper::toImportedData).collect(Collectors.toList());
        return StreamSupport.stream(importedDataRepository.saveAll(importedData).spliterator(), false).collect(Collectors.toList());
    }
}