package com.codecool.navymanager.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "ship_classes_and_guns",
        uniqueConstraints = { @UniqueConstraint(columnNames = { "ship_class_id", "gun_id" }) })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

    public GunAndQuantity(Long id, Gun gun, Integer gunQuantity) {
        this.id = id;
        this.gun = gun;
        this.gunQuantity = gunQuantity;
    }

    @Override
    public String toString() {
        return "GunAndQuantity{" +
                "id=" + id +
                ", shipClass=" + shipClass +
                ", gun=" + gun +
                ", gunQuantity=" + gunQuantity +
                '}';
    }
}