package com.codecool.navymanager.entityDTO;

import com.codecool.navymanager.entity.Country;

import java.io.Serializable;

public record CountryDto(Long id, String name) implements Serializable {
    private static final long serialVersionUID = 1L;

    public CountryDto(Country country) {
        this(country.getId(), country.getName());
    }
}
