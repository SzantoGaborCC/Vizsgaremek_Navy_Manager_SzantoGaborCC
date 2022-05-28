package com.codecool.navymanager.spring_converters;

import com.codecool.navymanager.DTO.RankDTO;
import com.codecool.navymanager.service.RankService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PrecedenceToRankDTOConverter implements Converter<String, RankDTO> {
    private final RankService rankService;

    public PrecedenceToRankDTOConverter(RankService rankService) {
        this.rankService = rankService;
    }

    @Override
    public RankDTO convert(String source) {
        return rankService.findAll().stream()
                .filter(rankDTO -> rankDTO.getPrecedence() == (Integer.valueOf(source)))
                .findAny().orElseThrow();
    }
}
