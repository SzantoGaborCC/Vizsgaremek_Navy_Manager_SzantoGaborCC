package com.codecool.navymanager.entityDTO;

import com.codecool.navymanager.entity.Fleet;
import com.codecool.navymanager.entity.Ship;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FleetDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String designation;
    private RankDto minimumRank;
    private OfficerDto commander;
    private CountryDto country;
    private Set<ShipDto> ships;

    public FleetDto(Fleet fleet) {
        id = fleet.getId();
        designation = fleet.getDesignation();
        minimumRank = new RankDto(fleet.getMinimumRank());
        commander = new OfficerDto(fleet.getCommander());
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
                minimumRank.toEntity(),
                commander.toEntity(),
                country.toEntity(),
                (ships == null) ? null :
                    ships.stream()
                            .map(shipDto -> shipDto.toEntity())
                            .collect(Collectors.toSet())
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
}
