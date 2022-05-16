package com.codecool.fleetmanager.DTO;

import com.codecool.fleetmanager.model.Country;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryDTO {
    private Long id;
    private String name;

    public CountryDTO(Country country) {
        this.id = country.getId();
        this.name = country.getName();
    }

    public Country convertToCountry() {
        return new Country(getId(), getName());
    }
}
