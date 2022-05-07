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
    private long countryId;

    public Gun(String designation, int caliberInMms, int projectileWeightInKgs, int rangeInMeters, long countryId) {
        this.designation = designation;
        this.caliberInMms = caliberInMms;
        this.projectileWeightInKgs = projectileWeightInKgs;
        this.rangeInMeters = rangeInMeters;
        this.countryId = countryId;
    }
}
