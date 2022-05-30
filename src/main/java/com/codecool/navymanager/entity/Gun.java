package com.codecool.navymanager.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "gun")
@Getter
@Setter
public class Gun {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "designation", nullable = false, length = 100)
    private String designation;

    @Column(name = "caliber_in_mms", nullable = false)
    private Integer caliberInMms;

    @Column(name = "projectile_weight_in_kgs", nullable = false)
    private Integer projectileWeightInKgs;

    @Column(name = "range_in_meters", nullable = false)
    private Integer rangeInMeters;

    @Column(name = "minimum_ship_displacement_in_tons", nullable = false)
    private Integer minimumShipDisplacementInTons;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gun that = (Gun) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}