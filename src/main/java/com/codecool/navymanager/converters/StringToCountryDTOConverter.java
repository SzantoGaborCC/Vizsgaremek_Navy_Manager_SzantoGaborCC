package com.codecool.navymanager.converters;

import com.codecool.navymanager.DTO.CountryDTO;
import com.codecool.navymanager.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToCountryDTOConverter implements Converter<String, CountryDTO> {
    private final CountryService countryService;

    public StringToCountryDTOConverter(CountryService countryService) {
        this.countryService = countryService;
    }

    @Override
    public CountryDTO convert(String source) {
        return countryService.findAll().stream()
                .filter(rankDTO -> rankDTO.getName().equals(source))
                .findAny().orElseThrow();
    }
}
