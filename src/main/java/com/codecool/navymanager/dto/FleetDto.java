package com.codecool.navymanager.dto;

import com.codecool.navymanager.entity.Fleet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FleetDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    @NotNull(message = "Designation must be specified and its length must be between 1 and 255!")
    @Size(min = 1, max = 255, message = "Designation length must be between 1 and 255!")
    private String designation;
    private OfficerDto commander;
    @NotNull(message = "You must choose a valid country!")
    private CountryDto country;
    private Set<ShipDto> ships;

    public FleetDto(Long id) {
        this.id = id;
    }

    public FleetDto(Fleet fleet) {
        id = fleet.getId();
        designation = fleet.getDesignation();
        commander = fleet.getCommander() != null ? new OfficerDto(fleet.getCommander()) : null;
        country = new CountryDto(fleet.getCountry());
        if (fleet.getShips() != null) {
            ships = fleet.getShips().stream()
                    .map(ShipDto::new)
                    .collect(Collectors.toSet());
        }
    }

    public Fleet toEntity() {
        return new Fleet(
                id,
                designation,
                commander != null ? commander.toEntity() : null,
                country != null ? country.toEntity() : null,
                ships != null ?
                        ships.stream()
                            .map(ShipDto::toEntity)
                            .collect(Collectors.toSet()) : null
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FleetDto that = (FleetDto) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "FleetDto{" +
                "id=" + id +
                ", designation='" + designation + '\'' +
                ", commander=" + commander +
                ", country=" + country +
                '}';
    }
}
