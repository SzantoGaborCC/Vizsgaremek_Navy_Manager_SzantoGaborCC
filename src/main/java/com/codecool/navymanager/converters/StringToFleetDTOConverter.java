package com.codecool.navymanager.converters;

import com.codecool.navymanager.DTO.FleetDTO;
import com.codecool.navymanager.DTO.ShipDTO;
import com.codecool.navymanager.service.FleetService;
import com.codecool.navymanager.service.ShipService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToFleetDTOConverter implements Converter<String, FleetDTO> {
    private final FleetService fleetService;

    public StringToFleetDTOConverter(FleetService fleetService) {
        this.fleetService = fleetService;
    }

    @Override
    public FleetDTO convert(String source) {
        return fleetService.findAll().stream()
                .filter(fleetDTO -> fleetDTO.getDesignation().equals(source))
                .findAny().orElseThrow();
    }
}
