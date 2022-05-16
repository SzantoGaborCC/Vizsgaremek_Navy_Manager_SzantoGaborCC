package com.codecool.fleetmanager.DTO;

import com.codecool.fleetmanager.model.Country;
import com.codecool.fleetmanager.model.Fleet;
import com.codecool.fleetmanager.model.Officer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FleetDTO {
    private Long id;
    private String designation;
    private int minimumRankPrecedence;
    private OfficerDTO commander;
    private CountryDTO country;
    private Set<ShipDTO> ships;

    public FleetDTO(Fleet fleet) {
        this.id = fleet.getId();
        this.designation = fleet.getDesignation();
        this.minimumRankPrecedence = fleet.getMinimumRankPrecedence();
    }

    public Fleet convertToFleet() {
        return new Fleet(
                getId(),getDesignation(), getMinimumRankPrecedence(),
                getCommander().getId(), getCountry().getId());
    }
}
