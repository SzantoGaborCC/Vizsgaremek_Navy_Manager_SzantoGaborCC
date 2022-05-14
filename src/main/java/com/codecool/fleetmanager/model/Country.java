package com.codecool.fleetmanager.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Country {
    private Long id;
    private String name;

    public Country(String name) {
        this.name = name;
    }
}
