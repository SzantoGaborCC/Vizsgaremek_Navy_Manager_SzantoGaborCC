package com.codecool.navymanager.converter;

import com.codecool.navymanager.dto.FleetDto;
import com.codecool.navymanager.service.FleetService;
import org.springframework.context.i18n.LocaleContextHolder;
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
        return fleetService.findById(Long.parseLong(source),  LocaleContextHolder.getLocale());
    }
}
