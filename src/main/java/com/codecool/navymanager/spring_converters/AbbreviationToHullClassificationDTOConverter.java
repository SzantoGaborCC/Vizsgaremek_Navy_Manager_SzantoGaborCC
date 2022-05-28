package com.codecool.navymanager.spring_converters;

import com.codecool.navymanager.DTO.HullClassificationDTO;
import com.codecool.navymanager.service.HullClassificationService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AbbreviationToHullClassificationDTOConverter implements Converter<String, HullClassificationDTO> {
    private final HullClassificationService hullClassificationService;

    public AbbreviationToHullClassificationDTOConverter(HullClassificationService hullClassificationService) {
        this.hullClassificationService = hullClassificationService;
    }

    @Override
    public HullClassificationDTO convert(String source) {
        return hullClassificationService.findAll().stream()
                .filter(hullClassificationDTO -> hullClassificationDTO.getAbbreviation().equals(source))
                .findAny().orElseThrow();
    }
}
