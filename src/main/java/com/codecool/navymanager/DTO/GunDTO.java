package com.codecool.navymanager.DTO;

import com.codecool.navymanager.model.Gun;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GunDTO {
    private Long id;
    private String designation;
    private int caliberInMms;
    private int projectileWeightInKgs;
    private int rangeInMeters;
    private int minimumShipDisplacementInTons;

    private CountryDTO country;

    public GunDTO(Gun gun) {
        this.id = gun.getId();
        this.designation = gun.getDesignation();
        this.caliberInMms = gun.getCaliberInMms();
        this.projectileWeightInKgs = gun.getProjectileWeightInKgs();
        this.rangeInMeters = gun.getRangeInMeters();
        this.minimumShipDisplacementInTons = gun.getMinimumShipDisplacementInTons();
    }

    public Gun convertToGun() {
        return new Gun(
                getDesignation(), getCaliberInMms(),
                getProjectileWeightInKgs(), getRangeInMeters(),
                getMinimumShipDisplacementInTons(), getCountry().getId());
    }
}