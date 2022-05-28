package com.codecool.navymanager.spring_converters;

import com.codecool.navymanager.DTO.ShipDTO;
import com.codecool.navymanager.service.ShipService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class IdToShipDTOConverter implements Converter<String, ShipDTO> {
    private final ShipService shipService;

    public IdToShipDTOConverter(ShipService shipService) {
        this.shipService = shipService;
    }

    @Override
    public ShipDTO convert(String source) {
        return shipService.findAll().stream()
                .filter(shipDTO -> shipDTO.getId().equals(Long.valueOf(source)))
                .findAny().orElseThrow();
    }
}

