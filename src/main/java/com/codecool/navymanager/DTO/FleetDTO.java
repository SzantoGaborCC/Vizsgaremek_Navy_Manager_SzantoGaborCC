package com.codecool.navymanager.DTO;

import com.codecool.navymanager.model.Fleet;
import com.codecool.navymanager.model.Rank;
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
    private RankDTO minimumRank;
    private OfficerDTO commander;
    private CountryDTO country;
    private Set<ShipDTO> ships;

    public FleetDTO(Fleet fleet) {
        this.id = fleet.getId();
        this.designation = fleet.getDesignation();
    }

    public Fleet convertToFleet() {
        return new Fleet(
                getId(),getDesignation(), getMinimumRank().getPrecedence(),
                getCommander().getId(), getCountry().getId());
    }
}
