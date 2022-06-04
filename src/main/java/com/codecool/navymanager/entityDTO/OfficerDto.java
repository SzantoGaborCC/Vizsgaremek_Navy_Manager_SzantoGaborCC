package com.codecool.navymanager.entityDTO;

import com.codecool.navymanager.entity.Officer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OfficerDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private LocalDate dateOfBirth;
    private RankDto rank;
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
