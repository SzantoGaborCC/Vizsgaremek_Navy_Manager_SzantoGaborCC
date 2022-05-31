package com.codecool.navymanager.entityDTO;


import com.codecool.navymanager.entity.Rank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RankDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer precedence;
    private String designation;

    public RankDto(Rank rank) {
        this(rank.getPrecedence(), rank.getDesignation());
    }

    public Rank toEntity() {
        return new Rank(precedence, designation);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RankDto that = (RankDto) o;
        return precedence != null && precedence.equals(that.precedence);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
