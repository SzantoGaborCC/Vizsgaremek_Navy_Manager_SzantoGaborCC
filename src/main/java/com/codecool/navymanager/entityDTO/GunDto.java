package com.codecool.navymanager.entityDTO;

import java.io.Serializable;

public record GunDto(Long id, String designation, Integer caliberInMms, Integer projectileWeightInKgs,
                     Integer rangeInMeters, Integer minimumShipDisplacementInTons,
                     CountryDto country) implements Serializable {
    private static final long serialVersionUID = 1L;
}
