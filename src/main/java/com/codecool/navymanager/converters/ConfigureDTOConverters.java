package com.codecool.navymanager.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ConfigureDTOConverters implements WebMvcConfigurer {
    @Autowired
    private StringToCountryDTOConverter stringToCountryDTOConverter;

    @Autowired
    private StringToRankDTOConverter stringToRankDTOConverter;

    @Autowired private StringToDateConverter stringToDateConverter;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(stringToCountryDTOConverter);
        registry.addConverter(stringToRankDTOConverter);
        registry.addConverter(stringToDateConverter);
    }
}
