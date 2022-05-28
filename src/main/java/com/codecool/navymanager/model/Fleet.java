package com.codecool.navymanager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fleet {
    private Long id;
    private String designation;
    private Integer minimumRankPrecedence;
    private Long commanderId;
    private Long countryId;

    public Fleet(String designation, int minimumRankPrecedence, long commanderId, long countryId) {
        this.designation = designation;
        this.minimumRankPrecedence = minimumRankPrecedence;
        this.commanderId = commanderId;
        this.countryId = countryId;
    }
}
