package com.codecool.navymanager.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "rank")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Rank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "precedence", nullable = false, unique=true)
    private Integer precedence;

    @Column(name = "designation", nullable = false, length = 100, unique=true)
    private String designation;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rank that = (Rank) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Rank{" +
                "precedence=" + precedence +
                ", designation='" + designation + '\'' +
                '}';
    }
}