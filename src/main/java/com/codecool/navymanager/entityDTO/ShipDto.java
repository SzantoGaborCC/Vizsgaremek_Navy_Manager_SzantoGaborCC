package com.codecool.navymanager.entityDTO;

import com.codecool.navymanager.entity.Ship;

import java.io.Serializable;

public record ShipDto(Long id, String name, ShipClassDto shipClass, OfficerDto captain,
                      CountryDto country) implements Serializable {
    private static final long serialVersionUID = 1L;

    public ShipDto(Ship ship) {
        this(
          ship.getId(),
          ship.getName(),
          new ShipClassDto(ship.getShipClass()),
          new OfficerDto(ship.getCaptain()),
          new CountryDto(ship.getCountry())
        );
    }
}
