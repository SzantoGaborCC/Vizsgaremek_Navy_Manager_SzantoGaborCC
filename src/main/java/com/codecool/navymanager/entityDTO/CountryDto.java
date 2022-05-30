package com.codecool.navymanager.entityDTO;

import java.io.Serializable;

public record CountryDto(Long id, String name) implements Serializable {
    private static final long serialVersionUID = 1L;
}
