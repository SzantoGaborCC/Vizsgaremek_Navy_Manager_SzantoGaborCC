package com.codecool.navymanager.entityDTO;

import com.codecool.navymanager.entity.Ship;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShipDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private ShipClassDto shipClass;
    private OfficerDto captain;
    private CountryDto country;

    public ShipDto(Ship ship) {
        id = ship.getId();
        name = ship.getName();
        shipClass = new ShipClassDto(ship.getShipClass());
        captain = new OfficerDto(ship.getCaptain());
        country = new CountryDto(ship.getCountry());
    }

    public Ship toEntity() {
        return new Ship(id, name, shipClass.toEntity(), captain.toEntity(), country.toEntity());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShipDto that = (ShipDto) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
