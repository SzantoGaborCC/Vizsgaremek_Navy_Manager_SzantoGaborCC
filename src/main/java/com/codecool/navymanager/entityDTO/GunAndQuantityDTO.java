package com.codecool.navymanager.entityDTO;

import java.io.Serializable;

public record GunAndQuantityDTO(GunDto gun, Integer quantity) implements Serializable {
    private static final long serialVersionUID = 1L;
}
