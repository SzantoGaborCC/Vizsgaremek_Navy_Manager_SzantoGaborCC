package com.codecool.fleetmanager.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Gun {
    private Long id;
    private String designation;
    private int caliberInMms;
    private int projectileWeightInKgs;
    private int rangeInMeters;
    private int minimumShipDisplacementInTons;
    private long countryId;

    public Gun(String designation, int caliberInMms, int projectileWeightInKgs,
               int rangeInMeters, int minimumShipDisplacementInTons, long countryId) {
        this.designation = designation;
        this.caliberInMms = caliberInMms;
        this.projectileWeightInKgs = projectileWeightInKgs;
        this.rangeInMeters = rangeInMeters;
        this.minimumShipDisplacementInTons = minimumShipDisplacementInTons;
        this.countryId = countryId;
    }
}
