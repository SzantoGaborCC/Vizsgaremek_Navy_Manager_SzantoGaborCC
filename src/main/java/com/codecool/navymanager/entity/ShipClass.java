package com.codecool.navymanager.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(
        name = "ship_class",
        uniqueConstraints = { @UniqueConstraint(
                name = "each_country_unique_ship_class_name", columnNames = { "name", "country_id" }) })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ShipClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "displacement_in_tons", nullable = false)
    private Integer displacementInTons;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hull_classification_id", nullable = false)
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

    @OneToMany(mappedBy = "shipClass", cascade = CascadeType.ALL)
    private Set<GunInstallation> guns;

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