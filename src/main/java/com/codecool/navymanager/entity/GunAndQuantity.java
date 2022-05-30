package com.codecool.navymanager.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "ship_classes_and_guns",
        uniqueConstraints = { @UniqueConstraint(columnNames = { "ship_class_id", "gun_id" }) })
@Getter
@Setter
public class GunAndQuantity {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GunAndQuantity that = (GunAndQuantity) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}