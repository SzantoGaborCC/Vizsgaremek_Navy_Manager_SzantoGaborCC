package com.codecool.navymanager.dto;


import com.codecool.navymanager.entity.Rank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class RankDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    @NotNull(message = "You must specify the precedence!")
    private Integer precedence;
    @NotNull(message = "Designation must be specified and its length must be between 1 and 255!")
    @Size(min = 1, max = 255, message = "Designation length must be between 1 and 255!")
    private String designation;

    public RankDto(Long id) {
        this.id = id;
    }

    public RankDto(Rank rank) {
        this(rank.getId(), rank.getPrecedence(), rank.getDesignation());
    }

    public Rank toEntity() {
        return new Rank(id, precedence, designation);
    }

    public RankDto(Long id, Integer precedence, String designation) {
        this.id = id;
        this.precedence = precedence;
        this.designation = designation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RankDto that = (RankDto) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "RankDto{" +
                "id=" + id +
                ", designation='" + designation + '\'' +
                '}';
    }
}
