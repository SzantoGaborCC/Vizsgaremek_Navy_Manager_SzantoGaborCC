package com.codecool.fleetmanager.service;

import com.codecool.fleetmanager.dao.CountryDao;
import com.codecool.fleetmanager.model.Country;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {
    private CountryDao countryDao;

    public CountryService(CountryDao countryDao) {
        this.countryDao = countryDao;
    }

    public List<Country> findAll() {
        return countryDao.findAll();
    }

    public Country findById(long id) {
        return countryDao.findById(id).orElseThrow();
    }

    public void add(Country country) {
        countryDao.add(country);
    }

    public void update(Country country, long id) {
        countryDao.update(country, id);
    }

    public void delete(long id) {
        countryDao.delete(id);
    }
}
