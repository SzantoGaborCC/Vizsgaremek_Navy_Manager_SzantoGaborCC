package com.codecool.navymanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginData {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
}
