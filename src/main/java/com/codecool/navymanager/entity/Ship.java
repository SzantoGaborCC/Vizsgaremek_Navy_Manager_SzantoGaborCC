package com.codecool.navymanager.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "ship", uniqueConstraints = {
                @UniqueConstraint(name = "each_ship_class_unique_ship_name", columnNames = { "name", "ship_class_id" })})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ship_class_id", nullable = false)
    private ShipClass shipClass;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "captain_id", referencedColumnName = "id")
    private Officer captain;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fleet_id")
    private Fleet fleet;

    public Ship(Long id, String name, ShipClass shipClass, Officer captain, Country country) {
        this.id = id;
        this.name = name;
        this.shipClass = shipClass;
        this.captain = captain;
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ship that = (Ship) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Ship{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", shipClass=" + shipClass +
                ", captain=" + captain +
                ", country=" + country +
                '}';
    }
}