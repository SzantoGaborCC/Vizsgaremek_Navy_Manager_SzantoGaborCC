package com.codecool.navymanager.DTO;

import com.codecool.navymanager.model.Ship;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShipClassAndGunWithQuantityDTO {
    private long shipClassId;
    private long gunId;
    private int gunQuantity;
}
