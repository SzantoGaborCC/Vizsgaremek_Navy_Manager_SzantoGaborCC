package com.codecool.navymanager.dao;

import com.codecool.navymanager.model.Country;

import java.util.List;
import java.util.Optional;

public interface CountryDao {
    List<Country> findAll();

    Optional<Country> findById(long id);

    void add(Country country);

    void update(Country country, long id);

    void delete(long id);
}
