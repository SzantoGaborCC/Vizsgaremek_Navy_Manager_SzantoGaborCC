package com.codecool.navymanager.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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

    @Column(name = "designation", nullable = false)
    private String designation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "minimum_rank_precedence", nullable = false)
    private Rank minimumRank;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "commander_id", nullable = false)
    private Officer commander;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @ManyToMany
    @JoinTable(name = "fleets_and_ships",
            joinColumns = @JoinColumn(name = "fleet_id"),
            inverseJoinColumns = @JoinColumn(name = "ship_id"))
    private Set<Ship> ships = new LinkedHashSet<>();

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
}