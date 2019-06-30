package com.ubs.interview.service.converter;

import org.csveed.bean.conversion.AbstractConverter;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class InstantConverter extends AbstractConverter<Instant> {

    public InstantConverter() {
        super(Instant.class);
    }

    @Override
    public Instant fromString(String text) {
        return Instant.from(DateTimeFormatter.ISO_DATE_TIME.parse(text));
    }

    @Override
    public String toString(Instant value) {
        return DateTimeFormatter.ISO_DATE_TIME.format(value);
    }
}