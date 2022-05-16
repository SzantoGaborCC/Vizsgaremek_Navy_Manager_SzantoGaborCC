package com.codecool.fleetmanager.DTO;

import com.codecool.fleetmanager.model.ShipClass;
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

    private HullClassificationDTO hullClassificationDTO;
    private int armorBeltInCms;
    private int armorTurretInCms;
    private int armorDeckInCms;
    private int speedInKmh;
    private CountryDTO country;
    private Map<GunDTO, Integer> guns;

    public ShipClassDTO(ShipClass shipClass) {
        this.id = shipClass.getId();
        this.name = shipClass.getName();
        this.armorBeltInCms = shipClass.getArmorBeltInCms();
        this.armorTurretInCms = shipClass.getArmorTurretInCms();
        this.armorDeckInCms = shipClass.getArmorDeckInCms();
        this.speedInKmh = shipClass.getSpeedInKmh();
    }

    public ShipClass convertToShipClass() {
        return new ShipClass(
                getId(), getName(),
                getDisplacementInTons(), getHullClassificationDTO().getAbbreviation(),
                getArmorBeltInCms(), getArmorTurretInCms(),
                getArmorDeckInCms(), getSpeedInKmh(),
                getCountry().getId());
    }
}
