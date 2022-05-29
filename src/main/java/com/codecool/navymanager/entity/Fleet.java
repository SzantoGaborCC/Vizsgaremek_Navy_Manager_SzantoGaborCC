package com.codecool.navymanager.entity;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "fleet")
public class Fleet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "designation", nullable = false)
    private String designation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "minimum_rank_precedence", nullable = false)
    private Rank minimumRankPrecedence;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "commander_id", nullable = false)
    private Officer commander;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @ManyToMany
    @JoinTable(name = "fleets_and_ships",
            joinColumns = @JoinColumn(name = "fleet_id"),
            inverseJoinColumns = @JoinColumn(name = "ship_id"))
    private Set<Ship> ships = new LinkedHashSet<>();

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

    public Rank getMinimumRankPrecedence() {
        return minimumRankPrecedence;
    }

    public void setMinimumRankPrecedence(Rank minimumRankPrecedence) {
        this.minimumRankPrecedence = minimumRankPrecedence;
    }

    public Officer getCommander() {
        return commander;
    }

    public void setCommander(Officer commander) {
        this.commander = commander;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Set<Ship> getShips() {
        return ships;
    }

    public void setShips(Set<Ship> ships) {
        this.ships = ships;
    }

}