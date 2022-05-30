package com.codecool.navymanager.entityDTO;

import com.codecool.navymanager.entity.GunAndQuantity;

import java.io.Serializable;

public record GunAndQuantityDto(GunDto gun, Integer quantity) implements Serializable {
    private static final long serialVersionUID = 1L;

    public GunAndQuantityDto(GunAndQuantity gunAndQuantity) {
        this(
                new GunDto(gunAndQuantity.getGun()),
                gunAndQuantity.getGunQuantity()
        );
    }
}
