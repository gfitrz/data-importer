package com.ubs.interview.service.dto;

import com.ubs.interview.service.converter.InstantConverter;
import com.ubs.interview.service.converter.TrimStringConverter;
import lombok.*;
import org.csveed.annotations.CsvCell;
import org.csveed.annotations.CsvConverter;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "primaryKey")
public class ImportedDataCsv {

    @CsvConverter(converter = TrimStringConverter.class)
    @CsvCell(columnName = "PRIMARY_KEY", required = true)
    private String primaryKey;

    @CsvCell(columnName = "NAME", required = true)
    private String name;

    @CsvCell(columnName = "DESCRIPTION", required = true)
    private String description;

    @CsvConverter(converter = InstantConverter.class)
    @CsvCell(columnName = "UPDATED_TIMESTAMP", required = true)
    private Instant updatedTimestamp;
}
