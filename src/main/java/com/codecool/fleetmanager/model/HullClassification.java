package com.codecool.fleetmanager.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HullClassification {
    private String abbreviation;
    private String designation;
    private int minimumRankPrecedence;
}
