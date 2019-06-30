package com.ubs.interview.service.mapper;

import com.ubs.interview.db.entity.ImportedData;
import com.ubs.interview.rest.response.ImportedDataResponse;
import com.ubs.interview.service.dto.ImportedDataCsv;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ImportedDataMapper {

    ImportedData toImportedData(ImportedDataCsv importedDataCsv);

    ImportedDataResponse toImportedDataResponse(ImportedData importedData);
}
