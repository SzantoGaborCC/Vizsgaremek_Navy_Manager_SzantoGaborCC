package com.codecool.navymanager.service;


import com.codecool.navymanager.entity.Fleet;
import com.codecool.navymanager.entity.Ship;
import com.codecool.navymanager.entityDTO.CountryDto;
import com.codecool.navymanager.entityDTO.FleetDto;
import com.codecool.navymanager.entityDTO.ShipDto;

import com.codecool.navymanager.repository.FleetRepository;
import com.codecool.navymanager.repository.ShipRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FleetService {
    private final FleetRepository fleetRepository;
    private final ShipRepository shipRepository;

    public FleetService(FleetRepository fleetRepository, ShipRepository shipRepository) {
        this.fleetRepository = fleetRepository;
        this.shipRepository = shipRepository;
    }

    public List<FleetDto> findAll() {
        return fleetRepository.findAll().stream().map(FleetDto::new).toList();
    }

    public FleetDto findById(long id) {
        return new FleetDto(fleetRepository.findById(id).orElseThrow());
    }

    public Set<ShipDto> findShips(long fleetId) {
        return fleetRepository.findById(fleetId).orElseThrow().getShips().stream()
                .map(ShipDto::new)
                .collect(Collectors.toSet());
    }

    @Transactional
    public void add(FleetDto fleetDto) {
        fleetRepository.save(fleetDto.toEntity());
    }

    @Transactional
    public void update(FleetDto fleetDto, long id) {
        if (fleetRepository.existsById(id)) {
            fleetRepository.save(fleetDto.toEntity());
        } else {
            throw new IllegalArgumentException("No such Fleet to update!");
        }
    }

    @Transactional
    public void deleteById(Long id) {
        fleetRepository.deleteById(id);
    }

    @Transactional
    public void addShipToFleet(Long fleetId, ShipDto shipDto) {
        try {
            Fleet fleet = fleetRepository.findById(fleetId).orElseThrow();
            Ship shipToAdd = shipRepository.findById(shipDto.getId()).orElseThrow();
            shipToAdd.setFleet(fleet);
            fleet.getShips().add(shipToAdd);
            fleetRepository.save(fleet);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid Fleet or Ship Id! " + e.getMessage());
        }
    }

    @Transactional
    public void updateShipForAFleet(long fleetId, long oldShipId, ShipDto newShipDto) {
        try {
            Fleet fleet = fleetRepository.findById(fleetId).orElseThrow();
            Ship oldShip = shipRepository.findById(oldShipId).orElseThrow();
            oldShip.setFleet(null);
            fleet.getShips().remove(oldShip);
            Ship newShip = newShipDto.toEntity();
            newShip.setFleet(fleet);
            fleet.getShips().add(newShip);
            fleetRepository.save(fleet);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid Fleet or Ship Id! " + e.getMessage());
        }
    }

    @Transactional
    public void deleteShipFromFleet(long fleetId, long shipId) {
        try {
            Fleet fleet = fleetRepository.findById(fleetId).orElseThrow();
            Ship shipToDelete = shipRepository.findById(shipId).orElseThrow();
            fleet.getShips().remove(shipToDelete);
            shipToDelete.setFleet(null);
            fleetRepository.save(fleet);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid Fleet or Ship Id!");
        }
    }
}
