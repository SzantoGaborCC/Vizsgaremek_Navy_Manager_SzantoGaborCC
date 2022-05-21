package com.codecool.navymanager.DTO;

import com.codecool.navymanager.model.Rank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RankDTO {
    private int precedence;
    private String designation;

    public RankDTO(Rank rank) {
        this.precedence = rank.getPrecedence();
        this.designation = rank.getDesignation();
    }

    public Rank convertToRank() {
        return new Rank(getPrecedence(), getDesignation());
    }
}
