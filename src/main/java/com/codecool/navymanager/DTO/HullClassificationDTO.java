package com.codecool.navymanager.DTO;

import com.codecool.navymanager.model.HullClassification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HullClassificationDTO {
    private String abbreviation;
    private String designation;
    private int minimumRankPrecedence;

    public HullClassificationDTO(HullClassification hullClassification) {
        this.abbreviation = hullClassification.getAbbreviation();
        this.designation = hullClassification.getDesignation();
        this.minimumRankPrecedence = hullClassification.getMinimumRankPrecedence();
    }

    public HullClassification convertToHullClassification() {
        return new HullClassification(
                getAbbreviation(), getDesignation(),getMinimumRankPrecedence()
        );
    }
}
