package com.codecool.navymanager.spring_converters;


import com.codecool.navymanager.entityDTO.ShipDto;
import com.codecool.navymanager.service.ShipService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class IdToShipDtoConverter implements Converter<String, ShipDto> {
    private final ShipService shipService;

    public IdToShipDtoConverter(ShipService shipService) {
        this.shipService = shipService;
    }

    @Override
    public ShipDto convert(String source) {
        return shipService.findAll().stream()
                .filter(shipDto -> shipDto.getId().equals(Long.valueOf(source)))
                .findAny().orElseThrow();
    }
}

