package com.codecool.navymanager.dto;

import com.codecool.navymanager.entity.HullClassification;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HullClassificationDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    @NotNull(message = "Abbreviation must be specified and its length must be between 1 and 4!")
    @Size(min = 1, max = 4, message = "Abbreviation length must be between 1 and 4!")
    private String abbreviation;
    @NotNull(message = "Designation must be specified and its length must be between 1 and 255!")
    @Size(min = 1, max = 255, message = "Designation length must be between 1 and 255!")
    private String designation;

    public HullClassificationDto(Long id) {
        this.id = id;
    }

    public HullClassificationDto(HullClassification hullClassification) {
        id = hullClassification.getId();
        abbreviation = hullClassification.getAbbreviation();
        designation = hullClassification.getDesignation();
    }

    public HullClassification toEntity() {
        return new HullClassification(
                id, abbreviation, designation
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

    @Override
    public String toString() {
        return "HullClassificationDto{" +
                "id=" + id +
                ", designation='" + designation + '\'' +
                '}';
    }
}
