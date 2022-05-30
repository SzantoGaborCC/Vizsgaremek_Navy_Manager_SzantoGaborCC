package com.codecool.navymanager.entityDTO;

import java.io.Serializable;

public record HullClassificationDto(String id, String designation,
                                    RankDto minimumRankPrecedence) implements Serializable {
    private static final long serialVersionUID = 1L;
}
