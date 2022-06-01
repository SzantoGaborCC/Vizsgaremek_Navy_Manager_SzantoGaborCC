package com.codecool.navymanager.spring_converters;

import com.codecool.navymanager.entityDTO.HullClassificationDto;
import com.codecool.navymanager.service.HullClassificationService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AbbreviationToHullClassificationDtoConverter implements Converter<String, HullClassificationDto> {
    private final HullClassificationService hullClassificationService;

    public AbbreviationToHullClassificationDtoConverter(HullClassificationService hullClassificationService) {
        this.hullClassificationService = hullClassificationService;
    }

    @Override
    public HullClassificationDto convert(String source) {
        return hullClassificationService.findAll().stream()
                .filter(hullClassificationDTO -> hullClassificationDTO.getAbbreviation().equals(source))
                .findAny().orElseThrow();
    }
}
