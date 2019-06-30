package com.ubs.interview.db.repository;

import com.ubs.interview.db.entity.ImportedData;
import org.springframework.data.repository.CrudRepository;

public interface ImportedDataRepository extends CrudRepository<ImportedData, String> {}