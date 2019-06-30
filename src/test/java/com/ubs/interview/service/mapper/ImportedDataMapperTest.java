package com.ubs.interview.service.mapper;

import com.ubs.interview.db.entity.ImportedData;
import com.ubs.interview.rest.response.ImportedDataResponse;
import com.ubs.interview.util.ImportedDataMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
class ImportedDataMapperTest {

    private final ImportedDataMapper importedDataMapper = Mappers.getMapper(ImportedDataMapper.class);

    @Test
    void toImportedData_forFilledObject_returnsImportedData() {
        ImportedData importedData = importedDataMapper.toImportedData(ImportedDataMocks.createImportedDataCsvMock());

        assertNotNull(importedData);
        assertEquals(ImportedDataMocks.FIRST_PRIMARY_KEY_MOCK, importedData.getPrimaryKey());
        assertEquals(ImportedDataMocks.NAME_MOCK, importedData.getName());
        assertEquals(ImportedDataMocks.DESCRIPTION_MOCK, importedData.getDescription());
        assertEquals(ImportedDataMocks.UPDATED_TIMESTAMP_MOCK, importedData.getUpdatedTimestamp());
    }

    @Test
    void toImportedDataResponse_forFilledObject_returnsImportedDataResponse() {
        ImportedDataResponse importedDataResponse = importedDataMapper.toImportedDataResponse(ImportedDataMocks.createImportedDataMock());

        assertNotNull(importedDataResponse);
        assertEquals(ImportedDataMocks.FIRST_PRIMARY_KEY_MOCK, importedDataResponse.getPrimaryKey());
        assertEquals(ImportedDataMocks.NAME_MOCK, importedDataResponse.getName());
        assertEquals(ImportedDataMocks.DESCRIPTION_MOCK, importedDataResponse.getDescription());
        assertEquals(ImportedDataMocks.UPDATED_TIMESTAMP_MOCK, importedDataResponse.getUpdatedTimestamp());
    }
}
