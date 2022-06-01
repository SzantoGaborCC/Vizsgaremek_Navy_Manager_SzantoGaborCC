package com.codecool.navymanager.spring_converters;

import com.codecool.navymanager.entityDTO.FleetDto;
import com.codecool.navymanager.service.FleetService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class IdToFleetDtoConverter implements Converter<String, FleetDto> {
    private final FleetService fleetService;

    public IdToFleetDtoConverter(FleetService fleetService) {
        this.fleetService = fleetService;
    }

    @Override
    public FleetDto convert(String source) {
        return fleetService.findAll().stream()
                .filter(fleetDTO -> fleetDTO.getId().equals(Long.valueOf(source)))
                .findAny().orElseThrow();
    }
}
