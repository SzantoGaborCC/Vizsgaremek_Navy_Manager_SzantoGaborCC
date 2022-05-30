package com.codecool.navymanager.entityDTO;

import java.io.Serializable;
import java.util.Set;

public record ShipClassDto(Long id, String name, Integer displacementInTons, HullClassificationDto hullClassification,
                           Integer armorBeltInCms, Integer armorTurretInCms, Integer armorDeckInCms, Integer speedInKmh,
                           CountryDto country, Set<GunAndQuantityDTO> shipClassesAndGuns) implements Serializable {
    private static final long serialVersionUID = 1L;
}
