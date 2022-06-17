package com.codecool.navymanager.dto;

import com.codecool.navymanager.entity.Gun;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GunDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    @NotNull(message = "Designation must be specified and its length must be between 1 and 255!")
    @Size(min = 1, max = 255, message = "Designation length must be between 1 and 255!")
    private String designation;
    @NotNull(message = "You must specify the caliber!")
    @Min(value = 1, message = "Caliber must be at least 1 mm!")
    private Integer caliberInMms;
    @NotNull(message = "You must specify the projectile weight!")
    @Min(value = 1, message = "Weight must be at least 1 kg!")
    private Integer projectileWeightInKgs;
    @NotNull(message = "You must specify the gun range!")
    @Min(value = 1, message = "Gun range must be at least 1 m!")
    private Integer rangeInMeters;
    @NotNull(message = "You must specify the minimum ship displacement!")
    @Min(value = 1, message = "Minimum ship displacement must be at least 1 t!")
    private Integer minimumShipDisplacementInTons;
    @NotNull(message = "You must specify the country!")
    private CountryDto country;

    public GunDto(Gun gun) {
        id = gun.getId();
        designation = gun.getDesignation();
        caliberInMms = gun.getCaliberInMms();
        projectileWeightInKgs = gun.getProjectileWeightInKgs();
        rangeInMeters = gun.getRangeInMeters();
        minimumShipDisplacementInTons = gun.getMinimumShipDisplacementInTons();
        country = new CountryDto(gun.getCountry());
    }

    public Gun toEntity() {
        return new Gun(
                id,
                designation,
                caliberInMms,
                projectileWeightInKgs,
                rangeInMeters,
                minimumShipDisplacementInTons,
                country.toEntity()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GunDto that = (GunDto) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "GunDto{" +
                "id=" + id +
                ", designation='" + designation + '\'' +
                '}';
    }
}
