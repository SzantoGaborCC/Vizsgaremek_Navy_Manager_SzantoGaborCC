package com.codecool.navymanager.dto;

import com.codecool.navymanager.entity.Officer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OfficerDto implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final OfficerDto UNASSIGNED_OFFICER = new OfficerDto(-1L, "----Unassigned----");

    private Long id;
    @NotNull(message = "Name must be specified and its length must be between 1 and 255!")
    @Size(min = 1, max = 255, message = "Name length must be between 1 and 255!")
    private String name;
    @Past
    private LocalDate dateOfBirth;
    @NotNull(message = "You must specify the rank!")
    private RankDto rank;
    @NotNull(message = "You must specify the country!")
    private CountryDto country;

    public OfficerDto(Officer officer) {
        id = officer.getId();
        name = officer.getName();
        dateOfBirth = officer.getDateOfBirth();
        rank = new RankDto(officer.getRank());
        country = new CountryDto(officer.getCountry());
    }

    public OfficerDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }


    public Officer toEntity() {
        return new Officer(id, name, dateOfBirth, rank.toEntity(), country.toEntity());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OfficerDto that = (OfficerDto) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
