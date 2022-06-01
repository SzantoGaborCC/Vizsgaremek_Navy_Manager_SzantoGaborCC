package com.codecool.navymanager.entityDTO;

import com.codecool.navymanager.entity.HullClassification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HullClassificationDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String abbreviation;
    private String designation;
    private RankDto minimumRank;

    public HullClassificationDto(HullClassification hullClassification) {
        abbreviation = hullClassification.getAbbreviation();
        designation = hullClassification.getDesignation();
        minimumRank = new RankDto(hullClassification.getMinimumRank());
    }

    public HullClassification toEntity() {
        return new HullClassification(
                abbreviation, designation, minimumRank.toEntity()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HullClassificationDto that = (HullClassificationDto) o;
        return abbreviation != null && abbreviation.equals(that.abbreviation);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
