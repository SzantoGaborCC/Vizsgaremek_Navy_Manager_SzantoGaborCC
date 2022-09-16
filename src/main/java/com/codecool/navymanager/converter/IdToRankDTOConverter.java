package com.codecool.navymanager.converter;


import com.codecool.navymanager.dto.RankDto;
import com.codecool.navymanager.service.RankService;
import org.springframework.context.i18n.LocaleContextHolder;
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
        return rankService.findById(Long.parseLong(source), LocaleContextHolder.getLocale());
    }
}
