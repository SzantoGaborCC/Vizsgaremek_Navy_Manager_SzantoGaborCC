package com.codecool.navymanager.DTO;

import com.codecool.navymanager.model.ShipClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShipClassDTO {
    private Long id;
    private String name;
    private int displacementInTons;

    private HullClassificationDTO hullClassification;
    private int armorBeltInCms;
    private int armorTurretInCms;
    private int armorDeckInCms;
    private int speedInKmh;
    private CountryDTO country;
    private Map<GunDTO, Integer> guns;

    public ShipClassDTO(ShipClass shipClass) {
        this.id = shipClass.getId();
        this.name = shipClass.getName();
        this.displacementInTons = shipClass.getDisplacementInTons();
        this.armorBeltInCms = shipClass.getArmorBeltInCms();
        this.armorTurretInCms = shipClass.getArmorTurretInCms();
        this.armorDeckInCms = shipClass.getArmorDeckInCms();
        this.speedInKmh = shipClass.getSpeedInKmh();
    }

    public ShipClass convertToShipClass() {
        return new ShipClass(
                getId(), getName(),
                getDisplacementInTons(), getHullClassification().getAbbreviation(),
                getArmorBeltInCms(), getArmorTurretInCms(),
                getArmorDeckInCms(), getSpeedInKmh(),
                getCountry().getId());
    }
}