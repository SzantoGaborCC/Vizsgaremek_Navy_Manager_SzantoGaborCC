package com.codecool.navymanager.service;


import com.codecool.navymanager.dto.FleetDto;
import com.codecool.navymanager.dto.OfficerDto;
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

    private final OfficerService officerService;

    public FleetService(FleetRepository fleetRepository, ShipRepository shipRepository, OfficerService officerService) {
        this.fleetRepository = fleetRepository;
        this.shipRepository = shipRepository;
        this.officerService = officerService;
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

    
    public void add(FleetDto fleetDto, Locale locale) {
        if (fleetRepository.existsById(fleetDto.getId()))
            throw new IllegalArgumentException("Add Fleet Error: Fleet already exists!");
        if (fleetDto.getCommander() != null && fleetDto.getCommander().getId() != -1) {
            OfficerDto officer = officerService.findById(fleetDto.getCommander().getId(), locale);
            if (officerService.isOfficerPostedToShipOrFleet(officer)) {
                fleetDto.setCommander(null);
                throw new IllegalArgumentException("Add Fleet Error: Officer is unavailable!");
            } else if (!fleetDto.getCountry().equals(officer.getCountry())) {
                throw new IllegalArgumentException("Add Fleet Error: Country mismatch!");
            }
        } else if (fleetDto.getCommander() != null && fleetDto.getCommander().getId() == -1) {
            fleetDto.setCommander(null);
        }
        fleetRepository.save(fleetDto.toEntity());
    }

    
    public void update(FleetDto newFleetData, long id, Locale locale) {
        if (newFleetData.getId() == null || newFleetData.getId() != id) {
            throw new IllegalArgumentException("Fleet Update error: Id cannot be null, and must match Id in the path!");
        }
        Fleet oldFleetData = fleetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Fleet Update error: Fleet must already be in the database!"));

        checkOfficerEligibilityBasedOFleetData(newFleetData, locale, oldFleetData);

        if (!newFleetData.getCountry().getId().equals(oldFleetData.getCountry().getId())) {
            for (Ship ship : oldFleetData.getShips()) {
                ship.setFleet(null);
            }
        }
        fleetRepository.save(newFleetData.toEntity());
    }


    private void checkOfficerEligibilityBasedOFleetData(FleetDto newFleetData, Locale locale, Fleet oldFleetData) {
        if (newFleetData.getCommander() != null && newFleetData.getCommander().getId() != -1) {
            OfficerDto newOfficer = officerService.findById(newFleetData.getCommander().getId(), locale);
            if (!newOfficer.getCountry().equals(newFleetData.getCountry())) {
                throw new IllegalArgumentException("Fleet Update Error: Country Mismatch!");
            }
            boolean isNewOfficerPosted = officerService.isOfficerPostedToShipOrFleet(newOfficer);
            if ((oldFleetData.getCommander() != null
                    && !oldFleetData.getCommander().getId().equals(newOfficer.getId())
                    && isNewOfficerPosted)
                    ||
                    (oldFleetData.getCommander() == null
                            && isNewOfficerPosted)
            ) {
                newFleetData.setCommander(null);
                throw new IllegalArgumentException("Fleet Update Error: Officer is not available!");
            }
        } else {
            newFleetData.setCommander(null);
        }
    }

    
    public void deleteById(Long id, Locale locale) {
        if (fleetRepository.existsById(id)) {
            fleetRepository.deleteById(id);
        } else {
            throw new NoSuchElementException("Delete Fleet Error : Fleet must already exist in the database!");
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
