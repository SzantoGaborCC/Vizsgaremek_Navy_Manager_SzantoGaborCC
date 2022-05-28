package com.codecool.navymanager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ship {
    private Long id;
    private String name;
    private Long shipClassId;
    private Long captainId;
    private Long countryId;

    public Ship(String name, Long shipClassId, Long captainId, Long countryId) {
        this.name = name;
        this.shipClassId = shipClassId;
        this.captainId = captainId;
        this.countryId = countryId;
    }
}
