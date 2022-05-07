package com.codecool.fleetmanager.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShipClass {
    private Long id;
    private String name;
    private int displacement_in_tons;
    private String hullClassification;
    private int armorBeltInCms;
    private int armorTurretInCms;
    private int armorDeckInCms;
    private int speedInKmh;
    private long countryId;

    public ShipClass(String name, int displacement_in_tons, String hullClassification,
                     int armorBeltInCms, int armorTurretInCms, int armorDeckInCms,
                     int speedInKmh, long countryId) {
        this.name = name;
        this.displacement_in_tons = displacement_in_tons;
        this.hullClassification = hullClassification;
        this.armorBeltInCms = armorBeltInCms;
        this.armorTurretInCms = armorTurretInCms;
        this.armorDeckInCms = armorDeckInCms;
        this.speedInKmh = speedInKmh;
        this.countryId = countryId;
    }
}
