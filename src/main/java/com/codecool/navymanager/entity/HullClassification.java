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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "abbreviation", nullable = false, length = 4, unique=true)
    private String abbreviation;

    @Column(name = "designation", nullable = false, unique=true)
    private String designation;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HullClassification that = (HullClassification) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}