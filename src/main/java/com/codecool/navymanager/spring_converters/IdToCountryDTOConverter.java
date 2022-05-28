package com.codecool.navymanager.spring_converters;

import com.codecool.navymanager.DTO.CountryDTO;
import com.codecool.navymanager.service.CountryService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class IdToCountryDTOConverter implements Converter<String, CountryDTO> {
    private final CountryService countryService;

    public IdToCountryDTOConverter(CountryService countryService) {
        this.countryService = countryService;
    }

    @Override
    public CountryDTO convert(String source) {
        return countryService.findAll().stream()
                .filter(rankDTO -> rankDTO.getId().equals(Long.valueOf(source)))
                .findAny().orElseThrow();
    }
}
