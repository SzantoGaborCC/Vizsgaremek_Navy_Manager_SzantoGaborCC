package com.codecool.navymanager.service;

import com.codecool.navymanager.dto.CountryDto;
import com.codecool.navymanager.entity.Country;
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
                .orElseThrow(() ->  new NoSuchElementException("Country Search Error: Could not find that Country in the database!")));
    }

    
    public void add(CountryDto countryDto) {
        countryRepository.save(countryDto.toEntity());
    }

    
    public void update(CountryDto countryDto, long id, Locale locale) {
        if (countryRepository.existsById(id) && countryDto.getId() == id) {
            countryRepository.save(countryDto.toEntity());
        } else {
            throw new IllegalArgumentException("Country Update Error: Id is not valid!");
        }
    }

    
    public void deleteById(Long id, Locale locale) {
        if (countryRepository.existsById(id)) {
            countryRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Country Deletion Error: Could not find that Country in the database!");
        }
    }
}
