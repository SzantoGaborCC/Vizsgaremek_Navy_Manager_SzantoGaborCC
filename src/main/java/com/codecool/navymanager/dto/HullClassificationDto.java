package com.codecool.navymanager.dto;

import com.codecool.navymanager.entity.HullClassification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HullClassificationDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    @NotNull(message = "Abbreviation must be specified and its length must be between 1 and 4!")
    @Size(min = 1, max = 4, message = "Abbreviation length must be between 1 and 4!")
    private String abbreviation;
    @NotNull(message = "Designation must be specified and its length must be between 1 and 255!")
    @Size(min = 1, max = 255, message = "Designation length must be between 1 and 255!")
    private String designation;
    @NotNull(message = "You must specify the minimum rank!")
    private RankDto minimumRank;

    public HullClassificationDto(HullClassification hullClassification) {
        id = hullClassification.getId();
        abbreviation = hullClassification.getAbbreviation();
        designation = hullClassification.getDesignation();
        minimumRank = new RankDto(hullClassification.getMinimumRank());
    }

    public HullClassification toEntity() {
        return new HullClassification(
                id, abbreviation, designation, minimumRank.toEntity()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HullClassificationDto that = (HullClassificationDto) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}