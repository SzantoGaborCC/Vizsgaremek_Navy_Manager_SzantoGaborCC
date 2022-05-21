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
    private int rank;
    private long countryId;

    public Officer(String name, Date dateOfBirth, int rank, long countryId) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.rank = rank;
        this.countryId = countryId;
    }
}
