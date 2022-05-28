package com.codecool.navymanager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShipClass {
    private Long id;
    private String name;
    private int displacementInTons;
    private String hullClassification;
    private int armorBeltInCms;
    private int armorTurretInCms;
    private int armorDeckInCms;
    private int speedInKmh;
    private Long countryId;

    public ShipClass(String name, int displacementInTons, String hullClassification,
                     int armorBeltInCms, int armorTurretInCms, int armorDeckInCms,
                     int speedInKmh, Long countryId) {
        this.name = name;
        this.displacementInTons = displacementInTons;
        this.hullClassification = hullClassification;
        this.armorBeltInCms = armorBeltInCms;
        this.armorTurretInCms = armorTurretInCms;
        this.armorDeckInCms = armorDeckInCms;
        this.speedInKmh = speedInKmh;
        this.countryId = countryId;
    }
}
