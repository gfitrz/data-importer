package com.ubs.interview.rest.endpoint;

import com.ubs.interview.rest.response.ErrorResponse;
import com.ubs.interview.rest.response.ImportedDataResponse;
import com.ubs.interview.service.ImportedDataService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("data")
public class DataRestController {

    private final ImportedDataService importedDataService;

    @ApiOperation("Store data from plain text file to database.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Operation successful.", response = ImportedDataResponse[].class),
        @ApiResponse(code = 409, message = "File contains duplicated primary key.", response = ErrorResponse.class),
        @ApiResponse(code = 422, message = "File contains some errors.", response = ErrorResponse.class)
    })
    @PostMapping("upload")
    public ResponseEntity<List<ImportedDataResponse>> uploadData(@RequestParam("file") MultipartFile plainTextFile) throws IOException {
        return ResponseEntity.ok(importedDataService.saveDataFromFile(plainTextFile.getInputStream()));
    }

    @ApiOperation("Retrieve all records from database.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Operation successful.", response = ImportedDataResponse[].class),
    })
    @GetMapping("all")
    public ResponseEntity<List<ImportedDataResponse>> getAllRecords() {
        return ResponseEntity.ok(importedDataService.getAll());
    }

    @ApiOperation("Retrieve a record by primary key.")
    @ApiResponses({
        @ApiResponse(code = 200, message = "Operation successful.", response = ImportedDataResponse.class),
        @ApiResponse(code = 404, message = "Record with given primaryKey not found.", response = ErrorResponse.class)
    })
    @GetMapping("{primaryKey}")
    public ResponseEntity<ImportedDataResponse> getRecord(@PathVariable("primaryKey") String primaryKey) {
        return ResponseEntity.ok(importedDataService.get(primaryKey));
    }

    @ApiOperation("Delete chosen record from database")
    @ApiResponses({
        @ApiResponse(code = 204, message = "Operation successful, no content returned."),
        @ApiResponse(code = 404, message = "Record with given primaryKey not found.", response = ErrorResponse.class)
    })
    @DeleteMapping("{primaryKey}")
    public ResponseEntity<Void> deleteRecord(@PathVariable("primaryKey") String primaryKey) {
        importedDataService.delete(primaryKey);
        return ResponseEntity.noContent().build();
    }
}