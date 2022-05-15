package com.codecool.fleetmanager.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
public class Fleet {
    private Long id;
    private String designation;
    private int minimumRankPrecedence;
    private long commanderId;
    private long countryId;

    private Set<Long> ships;

    public Fleet(String designation, int minimumRankPrecedence, long commanderId, long countryId, Set<Long> ships) {
        this.designation = designation;
        this.minimumRankPrecedence = minimumRankPrecedence;
        this.commanderId = commanderId;
        this.countryId = countryId;
        this.ships = ships;
    }
}
