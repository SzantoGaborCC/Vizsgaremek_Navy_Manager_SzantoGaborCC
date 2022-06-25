package com.codecool.navymanager.service;

import com.codecool.navymanager.dto.CountryDto;
import com.codecool.navymanager.entity.Country;
import com.codecool.navymanager.entity.Officer;
import com.codecool.navymanager.entity.Ship;
import com.codecool.navymanager.entity.ShipClass;
import com.codecool.navymanager.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

@Service

public class CountryService {
    @Autowired
    private MessageSource messageSource;

    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public List<CountryDto> findAll() {
        return countryRepository.findAll().stream()
                .map(CountryDto::new)
                .toList();
    }

    public CountryDto findById(long id, Locale locale) {
        return new CountryDto(countryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage(
                                "no_such",
                                new Object[] {Country.class.getSimpleName()},
                                locale))));
    }

    
    public void add(CountryDto countryDto, Locale locale) {
        if (countryDto.getId() != null && countryRepository.existsById(countryDto.getId())) {
            throw new IllegalArgumentException(messageSource.getMessage(
                            "add_error_must_not_exist",
                            new Object[]{Country.class.getSimpleName(), Country.class.getSimpleName()},
                            locale));
        }
        countryRepository.save(countryDto.toEntity());
    }

    
    public void update(CountryDto countryDto, long id, Locale locale) {
        if (countryDto.getId() == null || countryDto.getId() != id) {
            throw new IllegalArgumentException(messageSource.getMessage(
                            "update_error_id",
                            new Object[] {Country.class.getSimpleName()},
                            locale));
        }
        if (countryRepository.existsById(id)) {
            countryRepository.save(countryDto.toEntity());
        } else {
            throw new IllegalArgumentException(messageSource.getMessage(
                            "update_error_must_exist",
                            new Object[] {Country.class.getSimpleName(), Country.class.getSimpleName()},
                            locale));
        }
    }


    public void deleteById(Long id, Locale locale) {
        if (countryRepository.existsById(id)) {
            countryRepository.deleteById(id);
        } else {
            throw new NoSuchElementException(messageSource.getMessage(
                            "delete_error_must_exist",
                            new Object[] {Country.class.getSimpleName()},
                            locale));
        }
    }
}
