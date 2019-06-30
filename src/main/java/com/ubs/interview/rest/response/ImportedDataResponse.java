package com.ubs.interview.rest.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportedDataResponse {
    private String primaryKey;
    private String name;
    private String description;
    private Instant updatedTimestamp;
}
