package com.codecool.navymanager.spring_converters;

import com.codecool.navymanager.dto.CountryDto;
import com.codecool.navymanager.service.CountryService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class IdToCountryDtoConverter implements Converter<String, CountryDto> {
    private final CountryService countryService;

    public IdToCountryDtoConverter(CountryService countryService) {
        this.countryService = countryService;
    }

    @Override
    public CountryDto convert(String source) {
        return countryService.findAll().stream()
                .filter(rankDTO -> rankDTO.getId().equals(Long.valueOf(source)))
                .findAny().orElse(null);
    }
}
