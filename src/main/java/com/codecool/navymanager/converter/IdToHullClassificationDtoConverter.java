package com.codecool.navymanager.converter;

import com.codecool.navymanager.dto.HullClassificationDto;
import com.codecool.navymanager.service.HullClassificationService;
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
        return hullClassificationService.findAll().stream()
                .filter(hullClassificationDTO -> hullClassificationDTO.getId().equals(Long.valueOf(source)))
                .findAny().orElse(null);
    }
}
