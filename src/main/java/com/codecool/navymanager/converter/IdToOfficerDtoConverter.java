package com.codecool.navymanager.converter;

import com.codecool.navymanager.dto.OfficerDto;
import com.codecool.navymanager.service.OfficerService;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class IdToOfficerDtoConverter implements Converter<String, OfficerDto> {
    private final OfficerService officerService;

    public IdToOfficerDtoConverter(OfficerService officerService) {
        this.officerService = officerService;
    }

    @Override
    public OfficerDto convert(String source) {
        Locale locale = LocaleContextHolder.getLocale();
        return officerService.findById(Long.parseLong(source), locale);
    }
}
