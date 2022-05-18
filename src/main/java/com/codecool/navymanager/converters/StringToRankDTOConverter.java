package com.codecool.navymanager.converters;

import com.codecool.navymanager.DTO.RankDTO;
import com.codecool.navymanager.service.OfficerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToRankDTOConverter implements Converter<String, RankDTO> {
    @Autowired
    private OfficerService officerService;

    @Override
    public RankDTO convert(String source) {
        return officerService.getValidRankValues().stream()
                .filter(rankDTO -> rankDTO.getDesignation().equals(source))
                .findAny().orElseThrow();
    }
}
