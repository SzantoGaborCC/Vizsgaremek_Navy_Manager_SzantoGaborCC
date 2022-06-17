package com.codecool.navymanager.spring_converters;


import com.codecool.navymanager.dto.RankDto;
import com.codecool.navymanager.service.RankService;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class IdToRankDTOConverter implements Converter<String, RankDto> {
    private final RankService rankService;

    public IdToRankDTOConverter(RankService rankService) {
        this.rankService = rankService;
    }

    @Override
    public RankDto convert(String source) {
        return rankService.findAll().stream()
                .filter(rankDto -> rankDto.getId() == (Long.valueOf(source)))
                .findAny().orElse(null);
    }
}
