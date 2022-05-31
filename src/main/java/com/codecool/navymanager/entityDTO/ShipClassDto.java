package com.codecool.navymanager.entityDTO;

import com.codecool.navymanager.entity.ShipClass;
import lombok.*;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
                .map(GunAndQuantityDto::new)
                .collect(Collectors.toSet());
    }

    public ShipClass toEntity() {
        return new ShipClass(
                id,
                name,
                displacementInTons,
                hullClassification.toEntity(),
                armorBeltInCms,
                armorTurretInCms,
                armorDeckInCms,
                speedInKmh,
                country.toEntity(),
                guns.stream()
                        .map(gunAndQuantityDto ->
                                gunAndQuantityDto.toEntity())
                        .collect(Collectors.toSet())
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShipClassDto that = (ShipClassDto) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
