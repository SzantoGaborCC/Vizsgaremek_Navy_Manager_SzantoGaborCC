package com.codecool.navymanager.DTO;

import com.codecool.navymanager.model.HullClassification;
import com.codecool.navymanager.model.Rank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HullClassificationDTO {
    private String abbreviation;
    private String designation;
    private RankDTO minimumRank;

    public HullClassificationDTO(HullClassification hullClassification) {
        this.abbreviation = hullClassification.getAbbreviation();
        this.designation = hullClassification.getDesignation();
    }

    public HullClassification convertToHullClassification() {
        return new HullClassification(
                getAbbreviation(), getDesignation(), getMinimumRank().getPrecedence()
        );
    }
}
