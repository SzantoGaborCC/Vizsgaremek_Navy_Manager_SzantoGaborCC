package com.codecool.navymanager.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginData {
    @NotEmpty
    private String name;
    @NotEmpty
    private String password;
}
