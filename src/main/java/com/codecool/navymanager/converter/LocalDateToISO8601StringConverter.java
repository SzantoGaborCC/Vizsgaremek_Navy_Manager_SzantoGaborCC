package com.codecool.navymanager.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class LocalDateToISO8601StringConverter implements Converter<LocalDate, String> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public String convert(LocalDate source) {
           return source.format(formatter);
    }
}
