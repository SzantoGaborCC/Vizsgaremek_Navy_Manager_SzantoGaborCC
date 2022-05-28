package com.codecool.navymanager.spring_converters;

import com.codecool.navymanager.DTO.ShipDTO;
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
        System.out.println("--------------------------SHIP CONVERTER CALLED!!!!!!!!!!------------------------");
        System.out.println("ship name given to converter: " + source);
        var ship = shipService.findAll().stream()
                .filter(shipDTO -> shipDTO.getName().equals(source))
                .findAny().orElseThrow();
        System.out.println("Ship id found by shipService: " + ship.getId());
        return ship;
    }
}

