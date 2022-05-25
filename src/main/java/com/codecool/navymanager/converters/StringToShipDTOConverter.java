package com.codecool.navymanager.converters;

import com.codecool.navymanager.DTO.ShipClassDTO;
import com.codecool.navymanager.DTO.ShipDTO;
import com.codecool.navymanager.service.ShipClassService;
import com.codecool.navymanager.service.ShipService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToShipDTOConverter implements Converter<String, ShipDTO> {
    private final ShipService shipService;

    public StringToShipDTOConverter(ShipService shipService) {
        this.shipService = shipService;
    }

    @Override
    public ShipDTO convert(String source) {
        return shipService.findAll().stream()
                .filter(shipDTO -> shipDTO.getName().equals(source))
                .findAny().orElseThrow();
    }
}
