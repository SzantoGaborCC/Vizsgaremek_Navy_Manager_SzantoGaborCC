package com.codecool.navymanager.spring_converters;

import com.codecool.navymanager.DTO.OfficerDTO;
import com.codecool.navymanager.service.OfficerService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class IdToOfficerDTOConverter implements Converter<String, OfficerDTO> {
    private final OfficerService officerService;

    public IdToOfficerDTOConverter(OfficerService officerService) {
        this.officerService = officerService;
    }

    @Override
    public OfficerDTO convert(String source) {
        return officerService.findAll().stream()
                .filter(rankDTO -> rankDTO.getId().equals(Long.valueOf(source)))
                .findAny().orElseThrow();
    }
}
