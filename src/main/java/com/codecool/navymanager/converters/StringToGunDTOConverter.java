package com.codecool.navymanager.converters;

import com.codecool.navymanager.DTO.GunDTO;
import com.codecool.navymanager.DTO.ShipClassDTO;
import com.codecool.navymanager.service.GunService;
import com.codecool.navymanager.service.ShipClassService;
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
