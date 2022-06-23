package com.codecool.navymanager.service;


import com.codecool.navymanager.dto.FleetDto;
import com.codecool.navymanager.dto.ShipDto;
import com.codecool.navymanager.entity.Country;
import com.codecool.navymanager.entity.Fleet;
import com.codecool.navymanager.entity.Ship;
import com.codecool.navymanager.repository.FleetRepository;
import com.codecool.navymanager.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service

public class FleetService {
    @Autowired
    MessageSource messageSource;

    private final FleetRepository fleetRepository;
    private final ShipRepository shipRepository;

    public FleetService(FleetRepository fleetRepository, ShipRepository shipRepository) {
        this.fleetRepository = fleetRepository;
        this.shipRepository = shipRepository;
    }

    public List<FleetDto> findAll() {
        return fleetRepository.findAll().stream().map(FleetDto::new).toList();
    }

    public FleetDto findById(long id, Locale locale) {
        return new FleetDto(fleetRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        messageSource.getMessage(
                                "no_such",
                                new Object[] {Fleet.class.getSimpleName()},
                                locale))));
    }

    public Set<ShipDto> findShips(long fleetId, Locale locale) {
        return fleetRepository.findById(fleetId).orElseThrow(
                    () -> new NoSuchElementException(
                            messageSource.getMessage(
                                    "no_such",
                                    new Object[] {Fleet.class.getSimpleName()},
                                    locale))
                ).getShips().stream()
                .map(ShipDto::new)
                .collect(Collectors.toSet());
    }

    
    public void add(FleetDto fleetDto) {
        if (fleetDto.getCommander().getId() == -1)
            fleetDto.setCommander(null);
        fleetRepository.save(fleetDto.toEntity());
    }

    
    public void update(FleetDto fleetDto, long id, Locale locale) {
        Fleet fleet = fleetRepository.findById(id).orElseThrow(() -> new NoSuchElementException(
                messageSource.getMessage(
                "no_such",
                     new Object[] {Fleet.class.getSimpleName()},
                     locale)));
        if (!fleetDto.getCountry().getId().equals(fleet.getCountry().getId())) {
            for (Ship ship : fleet.getShips()) {
                ship.setFleet(null);
            }
        }
        if (fleetDto.getCommander().getId() == -1)
            fleetDto.setCommander(null);
        fleetRepository.save(fleetDto.toEntity());
    }

    
    public void deleteById(Long id, Locale locale) {
        if (fleetRepository.existsById(id)) {
            fleetRepository.deleteById(id);
        } else {
            throw  new NoSuchElementException(
                    messageSource.getMessage(
                            "no_such",
                            new Object[] {Fleet.class.getSimpleName()},
                            locale));
        }
    }

    
    public void addShipToFleet(Long fleetId, long shipId, Locale locale) {
            Fleet fleet = fleetRepository.findById(fleetId).orElseThrow(() -> new NoSuchElementException(
                    messageSource.getMessage(
                    "no_such",
                            new Object[] {Fleet.class.getSimpleName()},
                            locale)));
            Ship shipToAdd = shipRepository.findById(shipId).orElseThrow(() -> new NoSuchElementException(
                    messageSource.getMessage(
                    "no_such",
                            new Object[] {Ship.class.getSimpleName()},
                            locale)));
            shipToAdd.setFleet(fleet);
            shipRepository.save(shipToAdd);
    }

    
    public void updateShipInAFleet(long fleetId, long shipId, long newShipId, Locale locale) {
            removeShipFromFleet(fleetId, shipId, locale);
            addShipToFleet(fleetId, newShipId, locale);
    }

    
    public void removeShipFromFleet(long fleetId, long shipId, Locale locale) {
            Ship shipToRemove = shipRepository.findById(shipId)
                    .orElseThrow(() -> new NoSuchElementException(
                            messageSource.getMessage(
                                    "no_such",
                                    new Object[] {Ship.class.getSimpleName()},
                                    locale)));
            shipToRemove.setFleet(null);
            shipRepository.save(shipToRemove);
    }
}
