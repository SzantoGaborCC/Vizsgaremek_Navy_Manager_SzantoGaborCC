package com.codecool.navymanager.converters;

import com.codecool.navymanager.DTO.HullClassificationDTO;
import com.codecool.navymanager.DTO.RankDTO;
import com.codecool.navymanager.service.HullClassificationService;
import com.codecool.navymanager.service.RankService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToHullClassificationDTOConverter implements Converter<String, HullClassificationDTO> {
    private final HullClassificationService hullClassificationService;

    public StringToHullClassificationDTOConverter(HullClassificationService hullClassificationService) {
        this.hullClassificationService = hullClassificationService;
    }

    @Override
    public HullClassificationDTO convert(String source) {
        return hullClassificationService.findAll().stream()
                .filter(hullClassificationDTO -> hullClassificationDTO.getDesignation().equals(source))
                .findAny().orElseThrow();
    }
}
