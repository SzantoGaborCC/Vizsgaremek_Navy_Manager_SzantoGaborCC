package com.codecool.navymanager.spring_converters;


import com.codecool.navymanager.entityDTO.RankDto;
import com.codecool.navymanager.service.RankService;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PrecedenceToRankDTOConverter implements Converter<String, RankDto> {
    private final RankService rankService;

    public PrecedenceToRankDTOConverter(RankService rankService) {
        this.rankService = rankService;
    }

    @Override
    public RankDto convert(String source) {
        return rankService.findAll().stream()
                .filter(rankDto -> rankDto.getPrecedence() == (Integer.valueOf(source)))
                .findAny().orElseThrow();
    }
}
