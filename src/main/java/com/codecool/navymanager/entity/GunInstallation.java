package com.codecool.navymanager.entity;


import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "gun_installation",
        uniqueConstraints = {@UniqueConstraint(
                name = "each_ship_class_unique_gun_id", columnNames = { "ship_class_id", "gun_id" }) })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GunInstallation {
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
        GunInstallation that = (GunInstallation) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public GunInstallation(Long id, Gun gun, Integer gunQuantity) {
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