package com.codecool.navymanager.dto;

import com.codecool.navymanager.entity.Ship;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShipDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    @NotNull(message = "Name must be specified and its length must be between 1 and 255!")
    @Size(min = 1, max = 255, message = "Name length must be between 1 and 255!")
    private String name;
    @NotNull(message = "Ship class must be specified!")
    private ShipClassDto shipClass;

    private OfficerDto captain;
    @NotNull(message = "Country must be specified!")
    private CountryDto country;

    private FleetDto fleet;

    public ShipDto(Ship ship) {
        id = ship.getId();
        name = ship.getName();
        shipClass = new ShipClassDto(ship.getShipClass());
        captain = ship.getCaptain() != null ? new OfficerDto(ship.getCaptain()) : null;
        country = new CountryDto(ship.getCountry());
    }

    public Ship toEntity() {
        return new Ship(id, name, shipClass != null ? shipClass.toEntity() : null, captain != null ? captain.toEntity() : null, country.toEntity());
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

    @Override
    public String toString() {
        return "ShipDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", shipClass=" + shipClass +
                ", captain=" + captain +
                ", country=" + country +
                '}';
    }
}
