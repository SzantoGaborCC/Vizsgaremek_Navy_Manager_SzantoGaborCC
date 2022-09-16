package com.codecool.navymanager.converter;


import com.codecool.navymanager.dto.ShipDto;
import com.codecool.navymanager.service.ShipService;
import org.springframework.context.i18n.LocaleContextHolder;
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
        return shipService.findById(Long.parseLong(source), LocaleContextHolder.getLocale());
    }
}

