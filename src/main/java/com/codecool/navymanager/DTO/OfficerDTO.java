package com.codecool.navymanager.DTO;

import com.codecool.navymanager.model.Officer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfficerDTO {
    private Long id;
    private String name;
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$")
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
