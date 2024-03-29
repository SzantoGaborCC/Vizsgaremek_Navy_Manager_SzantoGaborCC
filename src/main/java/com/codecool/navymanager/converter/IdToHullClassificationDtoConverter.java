package com.codecool.navymanager.converter;

import com.codecool.navymanager.dto.HullClassificationDto;
import com.codecool.navymanager.service.HullClassificationService;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class IdToHullClassificationDtoConverter implements Converter<String, HullClassificationDto> {
    private final HullClassificationService hullClassificationService;

    public IdToHullClassificationDtoConverter(HullClassificationService hullClassificationService) {
        this.hullClassificationService = hullClassificationService;
    }

    @Override
    public HullClassificationDto convert(String source) {
        return hullClassificationService.findById(Long.parseLong(source), LocaleContextHolder.getLocale());
    }
}
