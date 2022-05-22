package com.codecool.navymanager.DTO;

import com.codecool.navymanager.model.Gun;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GunWithQuantityDTO {
    private GunDTO gun;
    private int gunQuantity;
}
