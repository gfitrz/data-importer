package com.ubs.interview.db.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "IMPORTED_DATA")
public class ImportedData {

    @Id
    @Column(name = "PRIMARY_KEY", unique = true, nullable = false)
    private String primaryKey;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "UPDATED_TIMESTAMP")
    private Instant updatedTimestamp;
}
