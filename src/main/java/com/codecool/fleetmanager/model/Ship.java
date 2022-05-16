package com.codecool.fleetmanager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ship {
    private Long id;
    private String name;
    private long shipClassId;
    private long captainId;
    private long countryId;

    public Ship(String name, long shipClassId, long captainId, long countryId) {
        this.name = name;
        this.shipClassId = shipClassId;
        this.captainId = captainId;
        this.countryId = countryId;
    }
}
