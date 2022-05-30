package com.codecool.navymanager.entityDTO;

import com.codecool.navymanager.entity.Gun;

import java.io.Serializable;

public record GunDto(Long id, String designation, Integer caliberInMms, Integer projectileWeightInKgs,
                     Integer rangeInMeters, Integer minimumShipDisplacementInTons,
                     CountryDto country) implements Serializable {
    private static final long serialVersionUID = 1L;

    public GunDto(Gun gun) {
        this(
                gun.getId(),
                gun.getDesignation(),
                gun.getCaliberInMms(),
                gun.getProjectileWeightInKgs(),
                gun.getRangeInMeters(),
                gun.getMinimumShipDisplacementInTons(),
                new CountryDto(gun.getCountry())
        );

    }
}
