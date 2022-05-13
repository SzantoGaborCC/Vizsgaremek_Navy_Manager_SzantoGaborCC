package com.codecool.fleetmanager.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Rank {
    private Long id;
    private String designation;
    private int precedence;
}
