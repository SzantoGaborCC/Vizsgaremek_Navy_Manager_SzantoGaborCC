package com.codecool.navymanager.entityDTO;

import com.codecool.navymanager.entity.ShipClass;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class ShipClassDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private Integer displacementInTons;
    private HullClassificationDto hullClassification;
    private Integer armorBeltInCms;
    private Integer armorTurretInCms;
    private Integer armorDeckInCms;
    private Integer speedInKmh;
    private CountryDto country;
    private Set<GunAndQuantityDto> guns;

    public ShipClassDto(ShipClass shipClass) {
               this.id = shipClass.getId();
               this.name = shipClass.getName();
               this.displacementInTons = shipClass.getDisplacementInTons();
               this.hullClassification = new HullClassificationDto(shipClass.getHullClassification());
               this.armorBeltInCms = shipClass.getArmorBeltInCms();
               this.armorTurretInCms = shipClass.getArmorTurretInCms();
               this.armorDeckInCms = shipClass.getArmorDeckInCms();
               this.speedInKmh = shipClass.getSpeedInKmh();
               this.country = new CountryDto(shipClass.getCountry());
               this.guns = shipClass.getGuns().stream()
                       .map(gunAndQuantity ->
                               new GunAndQuantityDto(gunAndQuantity))
                       .collect(Collectors.toSet());
    }
}
