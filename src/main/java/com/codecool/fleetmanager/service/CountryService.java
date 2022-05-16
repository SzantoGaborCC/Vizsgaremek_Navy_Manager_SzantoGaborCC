package com.codecool.fleetmanager.service;

import com.codecool.fleetmanager.DTO.CountryDTO;
import com.codecool.fleetmanager.dao.CountryDao;
import com.codecool.fleetmanager.model.Country;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CountryService {
    private CountryDao countryDao;

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
    public void update(Country country, long id) {
        countryDao.update(country, id);
    }

    @Transactional
    public void delete(long id) {
        countryDao.delete(id);
    }
}
