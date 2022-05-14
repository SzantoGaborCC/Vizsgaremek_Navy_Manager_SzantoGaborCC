package com.codecool.fleetmanager.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class Fleet {
    private Long id;
    private String designation;
    private int minimumRankPrecedence;
    private long commanderId;
    private long countryId;

    public Fleet(String designation, int minimumRankPrecedence, long commanderId, long countryId) {
        this.designation = designation;
        this.minimumRankPrecedence = minimumRankPrecedence;
        this.commanderId = commanderId;
        this.countryId = countryId;
    }
}
