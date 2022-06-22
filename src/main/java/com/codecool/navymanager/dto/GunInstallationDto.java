package com.codecool.navymanager.dto;

import com.codecool.navymanager.entity.GunInstallation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GunInstallationDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    @NotNull(message = "You must choose a valid gun!")
    private GunDto gun;
    @NotNull(message = "You must specify the quantity!")
    @Min(value = 1, message = "Quantity must be at least 1!")
    private Integer quantity;

    public GunInstallationDto(Long id) {
        this.id = id;
    }

    public GunInstallationDto(GunInstallation gunInstallation) {
                id = gunInstallation.getId();
                gun = new GunDto(gunInstallation.getGun());
                quantity = gunInstallation.getGunQuantity();
    }

    public GunInstallation toEntity() {
        return new GunInstallation(
                id,
                gun != null ? gun.toEntity() : null,
                quantity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GunInstallationDto that = (GunInstallationDto) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "GunAndQuantityDto{" +
                "id=" + id +
                ", gun=" + gun +
                ", quantity=" + quantity +
                '}';
    }
}
