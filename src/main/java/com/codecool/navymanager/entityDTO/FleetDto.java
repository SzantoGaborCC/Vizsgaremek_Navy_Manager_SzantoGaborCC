package com.codecool.navymanager.entityDTO;

import java.io.Serializable;
import java.util.Set;

public record FleetDto(Long id, String designation, RankDto minimumRankPrecedence, OfficerDto commander,
                       CountryDto country, Set<ShipDto> ships) implements Serializable {
    private static final long serialVersionUID = 1L;
}
