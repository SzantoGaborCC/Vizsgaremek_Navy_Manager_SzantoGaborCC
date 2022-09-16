package com.codecool.navymanager.converter;

import com.codecool.navymanager.dto.GunDto;
import com.codecool.navymanager.service.GunService;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class IdToGunDtoConverter implements Converter<String, GunDto> {
    private final GunService gunService;

    public IdToGunDtoConverter(GunService gunService) {
        this.gunService = gunService;
    }

    @Override
    public GunDto convert(String source) {
        Locale locale = LocaleContextHolder.getLocale();
        return gunService.findById(Long.parseLong(source), locale);
    }
}
