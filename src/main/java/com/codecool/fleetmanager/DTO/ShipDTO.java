package com.codecool.fleetmanager.DTO;

import com.codecool.fleetmanager.model.Ship;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShipDTO {
    private Long id;
    private String name;
    private ShipClassDTO shipClass;
    private OfficerDTO captain;
    private CountryDTO country;

    public ShipDTO(Ship ship) {
        this.id = ship.getId();
        this.name = ship.getName();
    }

    public Ship convertToShip() {
        return new Ship(
                getId(), getName(), getShipClass().getId(),
                getCaptain().getId(), getCountry().getId());
    }
}
