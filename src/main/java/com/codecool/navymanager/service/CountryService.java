package com.codecool.navymanager.service;

import com.codecool.navymanager.entityDTO.CountryDto;
import com.codecool.navymanager.repository.CountryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CountryService {
    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public List<CountryDto> findAll() {
        return countryRepository.findAll().stream()
                .map(CountryDto::new)
                .toList();
    }

    public CountryDto findById(long id) {
        return new CountryDto(countryRepository.findById(id).orElseThrow());
    }

    @Transactional
    public void add(CountryDto countryDto) {
        countryRepository.save(countryDto.toEntity());
    }

    @Transactional
    public void update(CountryDto countryDto, long id) {
        if (countryRepository.existsById(id)) {
            countryRepository.save(countryDto.toEntity());
        } else {
            throw new IllegalArgumentException("No such Country to update!");
        }
    }

    @Transactional
    public void deleteById(Long id) {
        countryRepository.deleteById(id);
    }
}
