package com.codecool.fleetmanager.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class Ship {
    private Long id;
    private String name;
    private long shipClassId;
    private long captainId;
    private long countryId;
    private Map<Long, Integer> guns;

    public Ship(String name, long shipClassId, long captainId, long countryId, Map<Long, Integer> guns) {
        this.name = name;
        this.shipClassId = shipClassId;
        this.captainId = captainId;
        this.countryId = countryId;
        this.guns = guns;
    }
}
