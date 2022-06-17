package com.codecool.navymanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdentityDto {
    @NotNull(message = "Chosen Id cannot be null!")
    private Long id;
}
