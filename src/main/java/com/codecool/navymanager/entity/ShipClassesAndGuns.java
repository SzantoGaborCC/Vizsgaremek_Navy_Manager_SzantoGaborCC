package com.codecool.navymanager.entity;

import javax.persistence.*;

@Entity
@Table(name = "ship_classes_and_guns", indexes = {
        @Index(name = "ship_classes_and_guns_index", columnList = "ship_class_id, gun_id", unique = true)
})
public class ShipClassesAndGuns {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ship_class_id", nullable = false)
    private ShipClass shipClass;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "gun_id", nullable = false)
    private Gun gun;

    @Column(name = "gun_quantity", nullable = false)
    private Integer gunQuantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ShipClass getShipClass() {
        return shipClass;
    }

    public void setShipClass(ShipClass shipClass) {
        this.shipClass = shipClass;
    }

    public Gun getGun() {
        return gun;
    }

    public void setGun(Gun gun) {
        this.gun = gun;
    }

    public Integer getGunQuantity() {
        return gunQuantity;
    }

    public void setGunQuantity(Integer gunQuantity) {
        this.gunQuantity = gunQuantity;
    }

}