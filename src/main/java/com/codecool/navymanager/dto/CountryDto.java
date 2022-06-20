package com.codecool.navymanager.dto;

import com.codecool.navymanager.entity.Country;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CountryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    @NotNull(message = "Name must be specified and its length must be between 1 and 255!")
    @Size(min = 1, max = 255, message = "Name length must be between 1 and 255!")
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
