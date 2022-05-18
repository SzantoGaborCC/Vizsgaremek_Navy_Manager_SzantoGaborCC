package com.codecool.navymanager.converters;

import com.codecool.navymanager.DTO.CountryDTO;
import com.codecool.navymanager.DTO.RankDTO;
import com.codecool.navymanager.service.OfficerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToCountryDTOConverter implements Converter<String, CountryDTO> {
    @Autowired
    private OfficerService officerService;

    @Override
    public CountryDTO convert(String source) {
        return officerService.getValidCountryValues().stream()
                .filter(rankDTO -> rankDTO.getName().equals(source))
                .findAny().orElseThrow();
    }
}
