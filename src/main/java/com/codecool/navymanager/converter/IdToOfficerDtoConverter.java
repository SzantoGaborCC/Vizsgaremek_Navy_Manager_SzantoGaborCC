package com.codecool.navymanager.converter;

import com.codecool.navymanager.dto.OfficerDto;
import com.codecool.navymanager.service.OfficerService;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class IdToOfficerDtoConverter implements Converter<String, OfficerDto> {
    private final OfficerService officerService;

    public IdToOfficerDtoConverter(OfficerService officerService) {
        this.officerService = officerService;
    }

    @Override
    public OfficerDto convert(String source) {
        return officerService.findById(Long.parseLong(source), LocaleContextHolder.getLocale());
    }
}
