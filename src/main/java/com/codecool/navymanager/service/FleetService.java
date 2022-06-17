package com.codecool.navymanager.service;


import com.codecool.navymanager.entity.Fleet;
import com.codecool.navymanager.entity.Ship;
import com.codecool.navymanager.dto.FleetDto;
import com.codecool.navymanager.dto.ShipDto;


import com.codecool.navymanager.repository.FleetRepository;
import com.codecool.navymanager.repository.ShipRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
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
        Fleet fleet = fleetRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No such fleet!"));
        if (!fleetDto.getCountry().getId().equals(fleet.getCountry().getId())) {
            for (Ship ship : fleet.getShips()) {
                ship.setFleet(null);
            }
            fleetDto.setCommander(null);
        }
        fleetRepository.save(fleetDto.toEntity());
    }

    @Transactional
    public void deleteById(Long id) {
        fleetRepository.deleteById(id);
    }

    @Transactional
    public void addShipToFleet(Long fleetId, long shipId) {
            Fleet fleet = fleetRepository.findById(fleetId).orElseThrow(() -> new NoSuchElementException("No such fleet!"));
            Ship shipToAdd = shipRepository.findById(shipId).orElseThrow(() -> new NoSuchElementException("No such ship!"));
            shipToAdd.setFleet(fleet);
            shipRepository.save(shipToAdd);
    }

    @Transactional
    public void updateShipInAFleet(long fleetId, long shipId, ShipDto newShipDto) {
            removeShipFromFleet(fleetId, shipId);
            addShipToFleet(fleetId, newShipDto.getId());
    }
//todo: IllegalArgumentExceptions should be handled by ControllerAdvice
    @Transactional
    public void removeShipFromFleet(long fleetId, long shipId) {
            Fleet fleet = fleetRepository.findById(fleetId).orElseThrow(() -> new IllegalArgumentException("No such fleet!"));
            Ship shipToRemove = shipRepository.findById(shipId).orElseThrow(() -> new IllegalArgumentException("No such ship!"));
            shipToRemove.setFleet(null);
            shipRepository.save(shipToRemove);
    }
}
