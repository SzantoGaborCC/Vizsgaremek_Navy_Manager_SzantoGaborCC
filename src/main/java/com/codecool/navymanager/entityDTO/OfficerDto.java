package com.codecool.navymanager.entityDTO;

import com.codecool.navymanager.entity.Officer;

import java.io.Serializable;
import java.time.LocalDate;

public record OfficerDto(Long id, String name, LocalDate dateOfBirth, RankDto rank,
                         CountryDto country) implements Serializable {
    private static final long serialVersionUID = 1L;

    public OfficerDto(Officer officer) {
        this(
                officer.getId(),
                officer.getName(),
                officer.getDateOfBirth(),
                new RankDto(officer.getRank()),
                new CountryDto(officer.getCountry())
        );
    }
}
