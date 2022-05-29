package com.codecool.navymanager.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rank")
public class Rank {
    @Id
    @Column(name = "precedence", nullable = false)
    private Integer id;

    @Column(name = "designation", nullable = false, length = 100)
    private String designation;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

}