package com.codecool.navymanager.converters;

import com.codecool.navymanager.DTO.OfficerDTO;
import com.codecool.navymanager.DTO.RankDTO;
import com.codecool.navymanager.service.OfficerService;
import com.codecool.navymanager.service.RankService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToOfficerDTOConverter implements Converter<String, OfficerDTO> {
    private final OfficerService officerService;

    public StringToOfficerDTOConverter(OfficerService officerService) {
        this.officerService = officerService;
    }

    @Override
    public OfficerDTO convert(String source) {
        return officerService.findAll().stream()
                .filter(rankDTO -> rankDTO.getName().equals(source))
                .findAny().orElseThrow();
    }
}
