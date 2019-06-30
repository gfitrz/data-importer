package com.ubs.interview.rest.endpoint;

import com.ubs.interview.service.ImportedDataService;
import com.ubs.interview.service.exception.CsvReadException;
import com.ubs.interview.service.exception.DuplicateKeyException;
import com.ubs.interview.service.exception.ObjectNotFoundException;
import com.ubs.interview.util.ImportedDataMocks;
import com.ubs.interview.util.TestExamples;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class DataRestControllerTest {

    private static final String DATA_ENDPOINT = "/data/{primaryKey}";
    private static final String DATA_ALL_ENDPOINT = "/data/all";
    private static final String DATA_UPLOAD_ENDPOINT = "/data/upload";

    private static final MockMultipartFile multipartFile = new MockMultipartFile("file", TestExamples.IMPORT_FILE_CORRECT_VALUES.getBytes());

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImportedDataService importedDataService;

    @Test
    void uploadData_forFileWithCorrectValues_returnsStatus200() throws Exception {
        Mockito.doReturn(List.of(ImportedDataMocks.createImportedDataResponseMock())).when(importedDataService).saveDataFromFile(any());
        mockMvc.perform(multipart(DATA_UPLOAD_ENDPOINT).file(multipartFile))
                .andExpect(status().isOk())
                .andExpect(content().json(TestExamples.IMPORTED_DATA_RESPONSE_ARRAY.getBody()));
    }

    @Test
    void uploadData_forFileWithDuplicatedValues_returnsStatus409() throws Exception {
        Mockito.doThrow(DuplicateKeyException.class).when(importedDataService).saveDataFromFile(any());
        mockMvc.perform(multipart(DATA_UPLOAD_ENDPOINT).file(multipartFile))
                .andExpect(status().isConflict());
    }

    @Test
    void uploadData_forFileWithIncorrectValues_returnsStatus422() throws Exception {
        Mockito.doThrow(CsvReadException.class).when(importedDataService).saveDataFromFile(any());
        mockMvc.perform(multipart(DATA_UPLOAD_ENDPOINT).file(multipartFile))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getAll_returnsStatus200() throws Exception {
        Mockito.doReturn(List.of(ImportedDataMocks.createImportedDataResponseMock())).when(importedDataService).getAll();
        mockMvc.perform(get(DATA_ALL_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().json(TestExamples.IMPORTED_DATA_RESPONSE_ARRAY.getBody()));
    }

    @Test
    void get_forExistingPrimaryKey_returnsStatus200() throws Exception {
        Mockito.doReturn(ImportedDataMocks.createImportedDataResponseMock()).when(importedDataService).get(ImportedDataMocks.FIRST_PRIMARY_KEY_MOCK);
        mockMvc.perform(get(DATA_ENDPOINT, ImportedDataMocks.FIRST_PRIMARY_KEY_MOCK))
                .andExpect(status().isOk())
                .andExpect(content().json(TestExamples.IMPORTED_DATA_RESPONSE.getBody()));
    }

    @Test
    void get_forNotExistingPrimaryKey_returnsStatus404() throws Exception {
        Mockito.doThrow(ObjectNotFoundException.class).when(importedDataService).get(ImportedDataMocks.FIRST_PRIMARY_KEY_MOCK);
        mockMvc.perform(get(DATA_ENDPOINT, ImportedDataMocks.FIRST_PRIMARY_KEY_MOCK)).andExpect(status().isNotFound());
    }

    @Test
    void delete_forExistingPrimaryKey_returnsStatus204() throws Exception {
        Mockito.doNothing().when(importedDataService).delete(ImportedDataMocks.FIRST_PRIMARY_KEY_MOCK);
        mockMvc.perform(delete(DATA_ENDPOINT, ImportedDataMocks.FIRST_PRIMARY_KEY_MOCK)).andExpect(status().isNoContent());
    }

    @Test
    void delete_forNotExistingPrimaryKey_returnsStatus404() throws Exception {
        Mockito.doThrow(ObjectNotFoundException.class).when(importedDataService).delete(ImportedDataMocks.FIRST_PRIMARY_KEY_MOCK);
        mockMvc.perform(delete(DATA_ENDPOINT, ImportedDataMocks.FIRST_PRIMARY_KEY_MOCK)).andExpect(status().isNotFound());
    }
}