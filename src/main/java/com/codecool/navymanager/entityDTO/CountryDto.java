package com.codecool.navymanager.entityDTO;

import com.codecool.navymanager.entity.Country;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CountryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;

    public CountryDto(Country country) {
        this(country.getId(), country.getName());
    }

    public Country toEntity() {
        return new Country(id, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountryDto that = (CountryDto) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
