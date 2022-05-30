package com.codecool.navymanager.entityDTO;

import java.io.Serializable;

public record ShipDto(Long id, String name, ShipClassDto shipClass, OfficerDto captain,
                      CountryDto country) implements Serializable {
    private static final long serialVersionUID = 1L;
}
