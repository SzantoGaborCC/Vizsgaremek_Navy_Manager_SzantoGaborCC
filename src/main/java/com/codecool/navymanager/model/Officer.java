package com.codecool.navymanager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Officer {
    private Long id;
    private String name;
    private Date dateOfBirth;
    private long rankId;
    private long countryId;

    public Officer(String name, Date dateOfBirth, long rankId, long countryId) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.rankId = rankId;
        this.countryId = countryId;
    }
}