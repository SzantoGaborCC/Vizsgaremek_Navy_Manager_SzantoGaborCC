package com.codecool.navymanager.converters;

import com.codecool.navymanager.DTO.RankDTO;
import com.codecool.navymanager.service.OfficerService;
import com.codecool.navymanager.service.RankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToRankDTOConverter implements Converter<String, RankDTO> {
    private final RankService rankService;

    public StringToRankDTOConverter(RankService rankService) {
        this.rankService = rankService;
    }

    @Override
    public RankDTO convert(String source) {
        return rankService.findAll().stream()
                .filter(rankDTO -> rankDTO.getDesignation().equals(source))
                .findAny().orElseThrow();
    }
}
