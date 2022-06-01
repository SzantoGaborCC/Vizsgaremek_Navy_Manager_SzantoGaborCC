package com.codecool.navymanager.spring_converters;

import com.codecool.navymanager.entityDTO.OfficerDto;
import com.codecool.navymanager.service.OfficerService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class IdToOfficerDtoConverter implements Converter<String, OfficerDto> {
    private final OfficerService officerService;

    public IdToOfficerDtoConverter(OfficerService officerService) {
        this.officerService = officerService;
    }

    @Override
    public OfficerDto convert(String source) {
        return officerService.findAll().stream()
                .filter(rankDTO -> rankDTO.getId().equals(Long.valueOf(source)))
                .findAny().orElseThrow();
    }
}
