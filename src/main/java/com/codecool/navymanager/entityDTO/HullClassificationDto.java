package com.codecool.navymanager.entityDTO;

import com.codecool.navymanager.entity.HullClassification;

import java.io.Serializable;

public record HullClassificationDto(String abbreviation, String designation,
                                    RankDto minimumRank) implements Serializable {
    private static final long serialVersionUID = 1L;

    HullClassificationDto(HullClassification hullClassification) {
        this(
                hullClassification.getAbbreviation(),
                hullClassification.getDesignation(),
                new RankDto(hullClassification.getMinimumRank())
        );
    }
}
