package com.codecool.navymanager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
