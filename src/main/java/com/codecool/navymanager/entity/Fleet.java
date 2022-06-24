package com.codecool.navymanager.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "fleet")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Fleet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull(message = "Designation length must be between 1 and 255!")
    @Size(min = 1, max = 255, message = "Designation length must be between 1 and 255!")
    @Column(name = "designation", unique = true)
    private String designation;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commander_id", referencedColumnName = "id")
    private Officer commander;

    @NotNull(message = "You must choose a valid country!")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country;

    @OneToMany(mappedBy = "fleet", cascade = CascadeType.PERSIST)
    private Set<Ship> ships;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fleet that = (Fleet) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Fleet{" +
                "id=" + id +
                ", designation='" + designation + '\'' +
                ", commander=" + commander +
                ", country=" + country +
                ", ships=" + ships +
                '}';
    }
}