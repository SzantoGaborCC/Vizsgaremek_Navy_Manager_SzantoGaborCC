package com.codecool.navymanager.entityDTO;

import com.codecool.navymanager.entity.Fleet;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record FleetDto(Long id, String designation, RankDto minimumRank, OfficerDto commander,
                       CountryDto country, Set<ShipDto> ships) implements Serializable {
    private static final long serialVersionUID = 1L;

    public FleetDto(Fleet fleet) {
        this(
                fleet.getId(),
                fleet.getDesignation(),
                new RankDto(fleet.getMinimumRank()),
                new OfficerDto(fleet.getCommander()),
                new CountryDto(fleet.getCountry()),
                fleet.getShips().stream()
                        .map(ship -> new ShipDto(ship))
                        .collect(Collectors.toSet())
        );
    }
}
