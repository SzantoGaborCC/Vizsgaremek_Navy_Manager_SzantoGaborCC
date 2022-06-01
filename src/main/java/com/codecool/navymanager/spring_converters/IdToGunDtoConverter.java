package com.codecool.navymanager.spring_converters;

import com.codecool.navymanager.entityDTO.GunDto;
import com.codecool.navymanager.service.GunService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class IdToGunDtoConverter implements Converter<String, GunDto> {
    private final GunService gunService;

    public IdToGunDtoConverter(GunService gunService) {
        this.gunService = gunService;
    }

    @Override
    public GunDto convert(String source) {
        return gunService.findAll().stream()
                .filter(shipClassDTO -> shipClassDTO.getId().equals(Long.valueOf(source)))
                .findAny().orElseThrow();
    }
}
