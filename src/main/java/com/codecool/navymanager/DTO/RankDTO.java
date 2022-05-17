package com.codecool.navymanager.DTO;

import com.codecool.navymanager.model.Rank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RankDTO {
    private Long id;
    private String designation;
    private int precedence;

    public RankDTO(Rank rank) {
        this.id = rank.getId();
        this.designation = rank.getDesignation();
        this.precedence = rank.getPrecedence();
    }

    public Rank convertToRank() {
        return new Rank(getId(), getDesignation(), getPrecedence());
    }
}
