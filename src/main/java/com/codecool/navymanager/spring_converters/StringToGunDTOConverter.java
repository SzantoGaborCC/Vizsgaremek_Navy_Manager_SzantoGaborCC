package com.codecool.navymanager.spring_converters;

import com.codecool.navymanager.DTO.GunDTO;
import com.codecool.navymanager.service.GunService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToGunDTOConverter implements Converter<String, GunDTO> {
    private final GunService gunService;

    public StringToGunDTOConverter(GunService gunService) {
        this.gunService = gunService;
    }

    @Override
    public GunDTO convert(String source) {
        return gunService.findAll().stream()
                .filter(shipClassDTO -> shipClassDTO.getDesignation().equals(source))
                .findAny().orElseThrow();
    }
}
