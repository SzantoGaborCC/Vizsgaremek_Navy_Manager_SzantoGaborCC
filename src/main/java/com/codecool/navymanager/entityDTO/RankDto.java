package com.codecool.navymanager.entityDTO;

import com.codecool.navymanager.entity.Rank;

import java.io.Serializable;

public record RankDto(Integer precedence, String designation) implements Serializable {
    private static final long serialVersionUID = 1L;

    RankDto(Rank rank) {
        this(rank.getPrecedence(), rank.getDesignation());
    }
}
