package com.codecool.navymanager.entityDTO;

import java.io.Serializable;

public record RankDto(Integer id, String designation) implements Serializable {
    private static final long serialVersionUID = 1L;
}
