package com.codecool.navymanager.entity;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "ship_class")
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
    private Set<ShipClassesAndGuns> shipClassesAndGuns = new LinkedHashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDisplacementInTons() {
        return displacementInTons;
    }

    public void setDisplacementInTons(Integer displacementInTons) {
        this.displacementInTons = displacementInTons;
    }

    public HullClassification getHullClassification() {
        return hullClassification;
    }

    public void setHullClassification(HullClassification hullClassification) {
        this.hullClassification = hullClassification;
    }

    public Integer getArmorBeltInCms() {
        return armorBeltInCms;
    }

    public void setArmorBeltInCms(Integer armorBeltInCms) {
        this.armorBeltInCms = armorBeltInCms;
    }

    public Integer getArmorTurretInCms() {
        return armorTurretInCms;
    }

    public void setArmorTurretInCms(Integer armorTurretInCms) {
        this.armorTurretInCms = armorTurretInCms;
    }

    public Integer getArmorDeckInCms() {
        return armorDeckInCms;
    }

    public void setArmorDeckInCms(Integer armorDeckInCms) {
        this.armorDeckInCms = armorDeckInCms;
    }

    public Integer getSpeedInKmh() {
        return speedInKmh;
    }

    public void setSpeedInKmh(Integer speedInKmh) {
        this.speedInKmh = speedInKmh;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Set<ShipClassesAndGuns> getShipClassesAndGuns() {
        return shipClassesAndGuns;
    }

    public void setShipClassesAndGuns(Set<ShipClassesAndGuns> shipClassesAndGuns) {
        this.shipClassesAndGuns = shipClassesAndGuns;
    }

}