package com.codecool.fleetmanager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HullClassification {
    private String abbreviation;
    private String designation;
    private int minimumRankPrecedence;
}
