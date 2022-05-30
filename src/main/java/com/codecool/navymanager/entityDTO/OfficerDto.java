package com.codecool.navymanager.entityDTO;

import java.io.Serializable;
import java.time.LocalDate;

public record OfficerDto(Long id, String name, LocalDate dateOfBirth, RankDto rank,
                         CountryDto country) implements Serializable {
    private static final long serialVersionUID = 1L;
}
