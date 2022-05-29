package com.codecool.navymanager.entity;

import javax.persistence.*;

@Entity
@Table(name = "gun")
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Integer getCaliberInMms() {
        return caliberInMms;
    }

    public void setCaliberInMms(Integer caliberInMms) {
        this.caliberInMms = caliberInMms;
    }

    public Integer getProjectileWeightInKgs() {
        return projectileWeightInKgs;
    }

    public void setProjectileWeightInKgs(Integer projectileWeightInKgs) {
        this.projectileWeightInKgs = projectileWeightInKgs;
    }

    public Integer getRangeInMeters() {
        return rangeInMeters;
    }

    public void setRangeInMeters(Integer rangeInMeters) {
        this.rangeInMeters = rangeInMeters;
    }

    public Integer getMinimumShipDisplacementInTons() {
        return minimumShipDisplacementInTons;
    }

    public void setMinimumShipDisplacementInTons(Integer minimumShipDisplacementInTons) {
        this.minimumShipDisplacementInTons = minimumShipDisplacementInTons;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

}