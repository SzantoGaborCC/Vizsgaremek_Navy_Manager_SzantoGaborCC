package com.codecool.navymanager.DTO;

import com.codecool.navymanager.model.Officer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfficerDTO {
    private Long id;
    private String name;
    private Date dateOfBirth;
    private RankDTO rank;
    private CountryDTO country;

    public OfficerDTO(Officer officer) {
        this.id = officer.getId();
        this.name = officer.getName();
        this.dateOfBirth = officer.getDateOfBirth();
    }

    public Officer convertToOfficer() {
        return new Officer(
                getId(), getName(),
                getDateOfBirth(), getRank().getId(),
                getCountry().getId());
    }
}
