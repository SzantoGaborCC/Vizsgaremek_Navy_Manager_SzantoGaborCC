package com.codecool.navymanager.converter;


import com.codecool.navymanager.dto.ShipClassDto;
import com.codecool.navymanager.service.ShipClassService;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class IdToShipClassDtoConverter implements Converter<String, ShipClassDto> {
    private final ShipClassService shipClassService;

    public IdToShipClassDtoConverter(ShipClassService shipClassService) {
        this.shipClassService = shipClassService;
    }

    @Override
    public ShipClassDto convert(String source) {
        return shipClassService.findById(Long.parseLong(source), LocaleContextHolder.getLocale());
    }
}
