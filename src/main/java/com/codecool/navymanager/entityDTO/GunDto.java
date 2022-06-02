package com.codecool.navymanager.entityDTO;

import com.codecool.navymanager.entity.Gun;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GunDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String designation;
    private Integer caliberInMms;
    private Integer projectileWeightInKgs;
    private Integer rangeInMeters;
    private Integer minimumShipDisplacementInTons;
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
