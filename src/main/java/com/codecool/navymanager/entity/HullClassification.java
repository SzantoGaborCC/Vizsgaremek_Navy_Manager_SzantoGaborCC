package com.codecool.navymanager.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "hull_classification")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HullClassification {
    @Id
    @Column(name = "abbreviation", nullable = false, length = 3)
    private String abbreviation;
    //todo: using an updatable id can break the api, should have a normal serial id

    @Column(name = "designation", nullable = false, length = 100)
    private String designation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "minimum_rank_precedence", nullable = false)
    private Rank minimumRank;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HullClassification that = (HullClassification) o;
        return abbreviation != null && abbreviation.equals(that.abbreviation);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}