package com.codecool.navymanager.spring_converters;

import com.codecool.navymanager.DTO.FleetDTO;
import com.codecool.navymanager.service.FleetService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class IdToFleetDTOConverter implements Converter<String, FleetDTO> {
    private final FleetService fleetService;

    public IdToFleetDTOConverter(FleetService fleetService) {
        this.fleetService = fleetService;
    }

    @Override
    public FleetDTO convert(String source) {
        return fleetService.findAll().stream()
                .filter(fleetDTO -> fleetDTO.getId().equals(Long.valueOf(source)))
                .findAny().orElseThrow();
    }
}