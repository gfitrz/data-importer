package com.ubs.interview.service;

import com.ubs.interview.db.entity.ImportedData;
import com.ubs.interview.db.repository.ImportedDataRepository;
import com.ubs.interview.rest.response.ImportedDataResponse;
import com.ubs.interview.service.dto.ImportedDataCsv;
import com.ubs.interview.service.exception.CsvReadException;
import com.ubs.interview.service.exception.DuplicateKeyException;
import com.ubs.interview.service.exception.ObjectNotFoundException;
import com.ubs.interview.util.ImportedDataMocks;
import com.ubs.interview.util.TestExamples;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class ImportedDataServiceTest {

    @Autowired
    private ImportedDataService importedDataService;

    @MockBean
    private ImportedDataRepository importedDataRepository;

    @Test
    void get_forNotExistingPrimaryKey_throwsObjectNotFoundException() {
        Mockito.doReturn(Optional.empty()).when(importedDataRepository).findById(ImportedDataMocks.FIRST_PRIMARY_KEY_MOCK);
        Assertions.assertThrows(ObjectNotFoundException.class, () -> importedDataService.get(ImportedDataMocks.FIRST_PRIMARY_KEY_MOCK));
    }

    @Test
    void get_forExistingPrimaryKey_returnsImportedDataResponse() {
        Mockito.doReturn(Optional.of(ImportedDataMocks.createImportedDataMock()))
                .when(importedDataRepository)
                .findById(ImportedDataMocks.FIRST_PRIMARY_KEY_MOCK);

        ImportedDataResponse importedDataResponse = importedDataService.get(ImportedDataMocks.FIRST_PRIMARY_KEY_MOCK);

        assertEquals(ImportedDataMocks.FIRST_PRIMARY_KEY_MOCK, importedDataResponse.getPrimaryKey());
        assertEquals(ImportedDataMocks.NAME_MOCK, importedDataResponse.getName());
        assertEquals(ImportedDataMocks.DESCRIPTION_MOCK, importedDataResponse.getDescription());
        assertEquals(ImportedDataMocks.UPDATED_TIMESTAMP_MOCK, importedDataResponse.getUpdatedTimestamp());
    }

    @Test
    void getAll_returnsListOfImportedDataResponse() {
        Mockito.doReturn(List.of(ImportedDataMocks.createImportedDataMock())).when(importedDataRepository).findAll();
        List<ImportedDataResponse> importedDataResponses = importedDataService.getAll();

        assertEquals(1, importedDataResponses.size());
        assertEquals(ImportedDataMocks.FIRST_PRIMARY_KEY_MOCK, importedDataResponses.get(0).getPrimaryKey());
        assertEquals(ImportedDataMocks.NAME_MOCK, importedDataResponses.get(0).getName());
        assertEquals(ImportedDataMocks.DESCRIPTION_MOCK, importedDataResponses.get(0).getDescription());
        assertEquals(ImportedDataMocks.UPDATED_TIMESTAMP_MOCK, importedDataResponses.get(0).getUpdatedTimestamp());
    }

    @Test
    void delete_forNotExistingPrimaryKey_throwsObjectNotFoundException() {
        Mockito.doThrow(EmptyResultDataAccessException.class).when(importedDataRepository).deleteById(ImportedDataMocks.FIRST_PRIMARY_KEY_MOCK);
        Assertions.assertThrows(ObjectNotFoundException.class, () -> importedDataService.delete(ImportedDataMocks.FIRST_PRIMARY_KEY_MOCK));
    }

    @Test
    void delete_forExistingPrimaryKey_returnsImportedDataResponse() {
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.doNothing().when(importedDataRepository).deleteById(ImportedDataMocks.FIRST_PRIMARY_KEY_MOCK);

        importedDataService.delete(ImportedDataMocks.FIRST_PRIMARY_KEY_MOCK);
        Mockito.verify(importedDataRepository).deleteById(argumentCaptor.capture());
        assertEquals(ImportedDataMocks.FIRST_PRIMARY_KEY_MOCK, argumentCaptor.getValue());
    }

    @Test
    void readDataFromFile_forFileWithCorrectValues_returnsListOfValues() {
        InputStream fileInputStream = TestExamples.IMPORT_FILE_CORRECT_VALUES.getInputStream();
        List<ImportedDataCsv> importedDataCsv = importedDataService.readDataFromFile(fileInputStream);

        assertEquals(2, importedDataCsv.size());
        assertEquals(ImportedDataMocks.FIRST_PRIMARY_KEY_MOCK, importedDataCsv.get(0).getPrimaryKey());
        assertEquals(ImportedDataMocks.NAME_MOCK, importedDataCsv.get(0).getName());
        assertEquals(ImportedDataMocks.DESCRIPTION_MOCK, importedDataCsv.get(0).getDescription());
        assertEquals(ImportedDataMocks.UPDATED_TIMESTAMP_MOCK, importedDataCsv.get(0).getUpdatedTimestamp());
    }

    @Test
    void readDataFromFile_forFileWithInCorrectValues_throwsCsvReadException() {
        InputStream fileInputStream = TestExamples.IMPORT_FILE_INCORRECT_VALUES.getInputStream();
        Assertions.assertThrows(CsvReadException.class, () -> importedDataService.readDataFromFile(fileInputStream));
    }

    @Test
    void checkIfThereAreDuplicates_forDuplicatedValues_throwsDuplicatedKeyException() {
        List<ImportedDataCsv> importedDataCsv = List.of(ImportedDataMocks.createImportedDataCsvMock(), ImportedDataMocks.createImportedDataCsvMock());
        Assertions.assertThrows(DuplicateKeyException.class, () -> importedDataService.checkIfThereAreDuplicates(importedDataCsv));
    }

    @Test
    void checkIfThereAreDuplicates_forCorrectValues_doesNotThrowException() {
        List<ImportedDataCsv> importedDataCsv = ImportedDataMocks.createListOfImportedDataCsvMock();
        Assertions.assertDoesNotThrow(() -> importedDataService.checkIfThereAreDuplicates(importedDataCsv));
    }

    @Test
    void saveCsvData_forCorrectValues_returnsSavedData() {
        List<ImportedDataCsv> importedDataCsv = ImportedDataMocks.createListOfImportedDataCsvMock();
        List<ImportedData> importedData = ImportedDataMocks.createListOfImportedDataMock();

        Mockito.doReturn(importedData).when(importedDataRepository).saveAll(importedData);

        List<ImportedData> result = importedDataService.saveCsvData(importedDataCsv);
        assertEquals(2, result.size());
        assertEquals(ImportedDataMocks.FIRST_PRIMARY_KEY_MOCK, result.get(0).getPrimaryKey());
        assertEquals(ImportedDataMocks.NAME_MOCK, result.get(0).getName());
        assertEquals(ImportedDataMocks.DESCRIPTION_MOCK, result.get(0).getDescription());
        assertEquals(ImportedDataMocks.UPDATED_TIMESTAMP_MOCK, result.get(0).getUpdatedTimestamp());
    }

    @Test
    void saveDataFromFile_forCorrectValues_returnsListOfImportedDataResponses() {
        List<ImportedData> importedData = ImportedDataMocks.createListOfImportedDataMock();

        Mockito.doReturn(importedData).when(importedDataRepository).saveAll(importedData);
        InputStream fileInputStream = TestExamples.IMPORT_FILE_CORRECT_VALUES.getInputStream();
        List<ImportedDataResponse> importedDataResponses = importedDataService.saveDataFromFile(fileInputStream);

        assertEquals(2, importedDataResponses.size());
        assertEquals(ImportedDataMocks.FIRST_PRIMARY_KEY_MOCK, importedDataResponses.get(0).getPrimaryKey());
        assertEquals(ImportedDataMocks.NAME_MOCK, importedDataResponses.get(0).getName());
        assertEquals(ImportedDataMocks.DESCRIPTION_MOCK, importedDataResponses.get(0).getDescription());
        assertEquals(ImportedDataMocks.UPDATED_TIMESTAMP_MOCK, importedDataResponses.get(0).getUpdatedTimestamp());
    }

    @Test
    void saveDataFromFile_forDuplicatedValues_throwsDuplicatedKeyException() {
        InputStream fileInputStream = TestExamples.IMPORT_FILE_DUPLICATED_VALUES.getInputStream();
        Assertions.assertThrows(DuplicateKeyException.class, () -> importedDataService.saveDataFromFile(fileInputStream));
    }

    @Test
    void saveDataFromFile_forIncorrectValues_throwsCsvReadException() {
        InputStream fileInputStream = TestExamples.IMPORT_FILE_INCORRECT_VALUES.getInputStream();
        Assertions.assertThrows(CsvReadException.class, () -> importedDataService.saveDataFromFile(fileInputStream));
    }
}
