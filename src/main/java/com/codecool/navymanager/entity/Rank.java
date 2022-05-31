package com.codecool.navymanager.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rank")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Rank {
    @Id
    @Column(name = "precedence", nullable = false)
    private Integer precedence;

    @Column(name = "designation", nullable = false, length = 100)
    private String designation;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rank that = (Rank) o;
        return precedence != null && precedence.equals(that.precedence);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}