package com.ubs.interview.util;

import com.ubs.interview.db.entity.ImportedData;
import com.ubs.interview.rest.response.ImportedDataResponse;
import com.ubs.interview.service.dto.ImportedDataCsv;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ImportedDataMocks {

    public static final String FIRST_PRIMARY_KEY_MOCK = "1";
    public static final String NAME_MOCK = "name";
    public static final String DESCRIPTION_MOCK = "value";
    public static final Instant UPDATED_TIMESTAMP_MOCK = Instant.from(DateTimeFormatter.ISO_DATE_TIME.parse("2019-06-21T13:27:04.143Z"));

    private static final String SECOND_PRIMARY_KEY_MOCK = "2";

    public static ImportedData createImportedDataMock() {
        return ImportedData.builder()
                .primaryKey(FIRST_PRIMARY_KEY_MOCK)
                .name(NAME_MOCK)
                .description(DESCRIPTION_MOCK)
                .updatedTimestamp(UPDATED_TIMESTAMP_MOCK)
                .build();
    }

    public static List<ImportedData> createListOfImportedDataMock() {
        ImportedData firstImportedData = createImportedDataMock();
        ImportedData secondImportedData = createImportedDataMock();
        secondImportedData.setPrimaryKey(SECOND_PRIMARY_KEY_MOCK);

        return List.of(firstImportedData, secondImportedData);
    }

    public static  ImportedDataCsv createImportedDataCsvMock() {
        return ImportedDataCsv.builder()
                .primaryKey(FIRST_PRIMARY_KEY_MOCK)
                .name(NAME_MOCK)
                .description(DESCRIPTION_MOCK)
                .updatedTimestamp(UPDATED_TIMESTAMP_MOCK)
                .build();
    }

    public static List<ImportedDataCsv> createListOfImportedDataCsvMock() {
        ImportedDataCsv firstImportedDataCsv = createImportedDataCsvMock();
        ImportedDataCsv secondImportedDataCsv = createImportedDataCsvMock();
        secondImportedDataCsv.setPrimaryKey(SECOND_PRIMARY_KEY_MOCK);

        return List.of(firstImportedDataCsv, secondImportedDataCsv);
    }

    public static ImportedDataResponse createImportedDataResponseMock() {
        return ImportedDataResponse.builder()
                .primaryKey(FIRST_PRIMARY_KEY_MOCK)
                .name(NAME_MOCK)
                .description(DESCRIPTION_MOCK)
                .updatedTimestamp(UPDATED_TIMESTAMP_MOCK)
                .build();
    }
}
