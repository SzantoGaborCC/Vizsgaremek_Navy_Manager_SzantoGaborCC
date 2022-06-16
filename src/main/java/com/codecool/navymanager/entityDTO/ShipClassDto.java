package com.codecool.navymanager.entityDTO;

import com.codecool.navymanager.entity.ShipClass;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @NotNull(message = "Name must be specified and its length must be between 1 and 255!")
    @Size(min = 1, max = 255, message = "name length must be between 1 and 255!")
    private String name;
    @NotNull(message = "You must specify the displacement!")
    @Min(value = 1, message = "Displacement must be at least 1 t!")
    private Integer displacementInTons;
    @NotNull(message = "You must specify the hull classification!")
    private HullClassificationDto hullClassification;
    @NotNull(message = "You must specify the armor belt thickness!")
    @Min(value = 1, message = "Armor belt thickness must be at least 1 mm!")
    private Integer armorBeltInCms;
    @NotNull(message = "You must specify the turret armor thickness!")
    @Min(value = 1, message = "Turret armor thickness must be at least 1 mm!")
    private Integer armorTurretInCms;
    @NotNull(message = "You must specify the armor deck thickness!")
    @Min(value = 1, message = "Armor deck thickness must be at least 1 mm!")
    private Integer armorDeckInCms;
    @NotNull(message = "You must specify the speed!")
    @Min(value = 1, message = "Speed must be at least 1 km/h!")
    private Integer speedInKmh;
    @NotNull(message = "You must specify the country!")
    private CountryDto country;
    private Set<GunInstallationDto> guns;

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
                .map(gunAndQuantity -> {
                    GunInstallationDto gunInstallationDto = new GunInstallationDto(gunAndQuantity);
                    gunInstallationDto.setShipClass(this);
                    return gunInstallationDto;
                })
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
                (guns == null) ? null :
                    guns.stream()
                        .map(gunInstallationDto -> gunInstallationDto.toEntity())
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
