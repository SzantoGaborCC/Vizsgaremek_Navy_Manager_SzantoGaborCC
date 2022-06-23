package com.codecool.navymanager.service;


import com.codecool.navymanager.dto.GunDto;
import com.codecool.navymanager.entity.Country;
import com.codecool.navymanager.entity.Gun;
import com.codecool.navymanager.repository.GunRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

@Service

public class GunService {
    @Autowired
    MessageSource messageSource;
    private final GunRepository gunRepository;

    public GunService(GunRepository gunRepository) {
        this.gunRepository = gunRepository;
    }

    public List<GunDto> findAll() {
        return gunRepository.findAll().stream().map(GunDto::new).toList();
    }

    public GunDto findById(long id, Locale locale) {
        return new GunDto(gunRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage(
                "no_such",
                new Object[] {Gun.class.getSimpleName()},
                locale))));
    }

    public List<GunDto> findByCountry(long countryId)  {
        return gunRepository.findByCountryId(countryId).stream().map(GunDto::new).toList();
    }

    
    public void add(GunDto gunDto) {
       gunRepository.save(gunDto.toEntity());
    }

    
    public void update(GunDto gunDto, long id, Locale locale) {
        if (gunRepository.existsById(id)) {
            gunRepository.save(gunDto.toEntity());
        } else {
            throw new NoSuchElementException(messageSource.getMessage(
                    "no_such",
                    new Object[] {Gun.class.getSimpleName()},
                    locale));
        }
    }

    
    public void deleteById(long id, Locale locale) {
        if (gunRepository.existsById(id)) {
            gunRepository.deleteById(id);
        } else {
            throw  new NoSuchElementException(
                    messageSource.getMessage(
                            "no_such",
                            new Object[] {Gun.class.getSimpleName()},
                            locale));
        }
    }
}
