package com.codecool.navymanager.spring_converters;

import com.codecool.navymanager.DTO.ShipClassDTO;
import com.codecool.navymanager.service.ShipClassService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToShipClassDTOConverter implements Converter<String, ShipClassDTO> {
    private final ShipClassService shipClassService;

    public StringToShipClassDTOConverter(ShipClassService shipClassService) {
        this.shipClassService = shipClassService;
    }

    @Override
    public ShipClassDTO convert(String source) {
        return shipClassService.findAll().stream()
                .filter(shipClassDTO -> shipClassDTO.getName().equals(source))
                .findAny().orElseThrow();
    }
}
