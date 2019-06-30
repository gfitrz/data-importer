package com.ubs.interview.service.converter;

import org.csveed.bean.conversion.AbstractConverter;
import org.springframework.util.StringUtils;

public class TrimStringConverter extends AbstractConverter<String> {

    public TrimStringConverter() {
        super(String.class);
    }

    @Override
    public String fromString(String text) {
        return StringUtils.trimWhitespace(text);
    }

    @Override
    public String toString(String value) {
        return value;
    }
}