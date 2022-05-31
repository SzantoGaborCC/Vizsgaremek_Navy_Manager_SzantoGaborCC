package com.codecool.navymanager.service;


import com.codecool.navymanager.entity.Fleet;
import com.codecool.navymanager.entity.Ship;
import com.codecool.navymanager.entityDTO.FleetDto;
import com.codecool.navymanager.entityDTO.ShipDto;

import com.codecool.navymanager.repository.FleetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FleetService {
    private final FleetRepository fleetRepository;

    public FleetService(FleetRepository fleetRepository) {
        this.fleetRepository = fleetRepository;
    }

    public List<FleetDto> findAll() {
        return fleetRepository.findAll().stream().map(FleetDto::new).toList();
    }

    public FleetDto findById(long id) {
        return new FleetDto(fleetRepository.findById(id).orElseThrow());
    }

    public Set<ShipDto> findShips(long fleetId) {
        return fleetRepository.findShips(fleetId).stream()
                .map(ShipDto::new)
                .collect(Collectors.toSet());
    }

    @Transactional
    public void add(FleetDto fleetDto) {
        fleetRepository.save(fleetDto.toEntity());
    }

    @Transactional
    public void update(FleetDto fleetDto) {
        fleetRepository.save(fleetDto.toEntity());
    }

    @Transactional
    public void deleteById(Long id) {
        fleetRepository.deleteById(id);
    }

    @Transactional
    public void addShipToFleet(Long fleetId, Ship ship) {
        try {
            Fleet fleet = fleetRepository.findById(fleetId).orElseThrow();
            fleet.getShips().add(ship);
            fleetRepository.save(fleet);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid Fleet or Ship Id!");
        }
    }

    @Transactional
    public void updateShipForAFleet(long fleetId, Ship oldShip, Ship newShip) {
        try {
            Fleet fleet = fleetRepository.findById(fleetId).orElseThrow();
            fleet.getShips().remove(oldShip);
            fleet.getShips().add(newShip);
            fleetRepository.save(fleet);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid Fleet or Ship Id!");
        }
    }

    @Transactional
    public void deleteShipFromFleet(long fleetId, Ship ship) {
        try {
            Fleet fleet = fleetRepository.findById(fleetId).orElseThrow();
            fleet.getShips().remove(ship);
            fleetRepository.save(fleet);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid Fleet or Ship Id!");
        }
    }
}
