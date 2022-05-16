package com.codecool.fleetmanager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Country {
    private Long id;
    private String name;

    public Country(String name) {
        this.name = name;
    }
}
