package com.codecool.navymanager.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "ship_class")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShipClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "displacement_in_tons", nullable = false)
    private Integer displacementInTons;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hull_classification", nullable = false)
    private HullClassification hullClassification;

    @Column(name = "armor_belt_in_cms", nullable = false)
    private Integer armorBeltInCms;

    @Column(name = "armor_turret_in_cms", nullable = false)
    private Integer armorTurretInCms;

    @Column(name = "armor_deck_in_cms", nullable = false)
    private Integer armorDeckInCms;

    @Column(name = "speed_in_kmh", nullable = false)
    private Integer speedInKmh;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @OneToMany(mappedBy = "shipClass")
    private Set<GunAndQuantity> guns = new LinkedHashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShipClass that = (ShipClass) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}