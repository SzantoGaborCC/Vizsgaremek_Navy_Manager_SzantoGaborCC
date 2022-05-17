package com.codecool.navymanager.service;

import com.codecool.navymanager.DTO.CountryDTO;
import com.codecool.navymanager.dao.CountryDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CountryService {
    private final CountryDao countryDao;

    public CountryService(CountryDao countryDao) {
        this.countryDao = countryDao;
    }

    public List<CountryDTO> findAll() {
        return countryDao.findAll().stream().map(CountryDTO::new).toList();
    }

    public CountryDTO findById(long id) {
        return new CountryDTO(countryDao.findById(id).orElseThrow());
    }

    @Transactional
    public void add(CountryDTO countryDTO) {
        countryDao.add(countryDTO.convertToCountry());
    }

    @Transactional
    public void update(CountryDTO countryDTO, long id) {
        countryDao.update(countryDTO.convertToCountry(), id);
    }

    @Transactional
    public void delete(long id) {
        countryDao.delete(id);
    }
}
