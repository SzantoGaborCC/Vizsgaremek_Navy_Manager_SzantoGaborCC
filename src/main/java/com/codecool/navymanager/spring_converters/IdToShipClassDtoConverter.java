package com.codecool.navymanager.spring_converters;


import com.codecool.navymanager.entityDTO.ShipClassDto;
import com.codecool.navymanager.service.ShipClassService;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class IdToShipClassDtoConverter implements Converter<String, ShipClassDto> {
    private final ShipClassService shipClassService;

    public IdToShipClassDtoConverter(ShipClassService shipClassService) {
        this.shipClassService = shipClassService;
    }

    @Override
    public ShipClassDto convert(String source) {
        return shipClassService.findAll().stream()
                .filter(shipClassDTO -> shipClassDTO.getId().equals(Long.valueOf(source)))
                .findAny().orElseThrow();
    }
}
