package com.codecool.navymanager.entity;

import javax.persistence.*;

@Entity
@Table(name = "hull_classification")
public class HullClassification {
    @Id
    @Column(name = "abbreviation", nullable = false, length = 3)
    private String id;

    @Column(name = "designation", nullable = false, length = 100)
    private String designation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "minimum_rank_precedence", nullable = false)
    private Rank minimumRankPrecedence;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

}