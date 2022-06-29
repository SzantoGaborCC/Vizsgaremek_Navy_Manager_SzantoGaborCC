package com.codecool.navymanager.service;


import com.codecool.navymanager.dto.FleetDto;
import com.codecool.navymanager.dto.OfficerDto;
import com.codecool.navymanager.dto.ShipDto;
import com.codecool.navymanager.entity.Country;
import com.codecool.navymanager.entity.Fleet;
import com.codecool.navymanager.entity.Officer;
import com.codecool.navymanager.entity.Ship;
import com.codecool.navymanager.repository.FleetRepository;
import com.codecool.navymanager.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

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
                .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage(
                                        "search_error_not_found",
                                        new Object[]{Fleet.class.getSimpleName()},
                                        locale))));
    }

    public List<ShipDto> findShips(long fleetId, Locale locale) {
        return fleetRepository.findById(fleetId)
                .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage(
                                    "search_error_not_found",
                                    new Object[]{Fleet.class.getSimpleName()},
                                    locale))
                ).getShips().stream()
                .map(ShipDto::new)
                .toList();
    }

    
    public void add(FleetDto fleetDto, Locale locale) {
        if (fleetDto.getId() != null && fleetRepository.existsById(fleetDto.getId()))
            throw new IllegalArgumentException(messageSource.getMessage(
                            "add_error_already_exist",
                            new Object[]{Fleet.class.getSimpleName(), Fleet.class.getSimpleName()},
                            locale));

        if (fleetDto.getCommander() != null && fleetDto.getCommander().getId() != -1) {
            OfficerDto officer = officerService.findById(fleetDto.getCommander().getId(), locale);
            if (officerService.isOfficerPostedToShipOrFleet(officer)) {
                fleetDto.setCommander(null);
                throw new IllegalArgumentException(messageSource.getMessage(
                                "add_error_unavailable",
                                new Object[]{Fleet.class.getSimpleName(), Officer.class.getSimpleName()},
                                locale));
            } else if (!fleetDto.getCountry().equals(officer.getCountry())) {
                throw new IllegalArgumentException(messageSource.getMessage(
                                "add_error_mismatch",
                                new Object[]{Fleet.class.getSimpleName(), Country.class.getSimpleName()},
                                locale));
            }
        } else if (fleetDto.getCommander() != null && fleetDto.getCommander().getId() == -1) {
            fleetDto.setCommander(null);
        }

        fleetRepository.save(fleetDto.toEntity());
    }

    
    public void update(FleetDto newFleetData, long id, Locale locale) {
        if (newFleetData.getId() == null || newFleetData.getId() != id) {
            throw new IllegalArgumentException(messageSource.getMessage(
                            "update_error_id",
                            new Object[]{Fleet.class.getSimpleName()},
                            locale));
        }
        Fleet oldFleetData = fleetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(messageSource.getMessage(
                                "update_error_must_exist",
                                new Object[]{Fleet.class.getSimpleName(), Fleet.class.getSimpleName()},
                                locale)));

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
                throw new IllegalArgumentException(messageSource.getMessage(
                                "update_error_mismatch",
                                new Object[]{Fleet.class.getSimpleName(), Country.class.getSimpleName()},
                                locale));
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
                throw new IllegalArgumentException(messageSource.getMessage(
                                "update_error_unavailable",
                                new Object[]{Fleet.class.getSimpleName(), Officer.class.getSimpleName()},
                                locale));
            }
        } else {
            newFleetData.setCommander(null);
        }
    }

    
    public void deleteById(Long id, Locale locale) {
        if (fleetRepository.existsById(id)) {
            fleetRepository.deleteById(id);
        } else {
            throw new NoSuchElementException(messageSource.getMessage(
                            "delete_error_must_exist",
                            new Object[]{Fleet.class.getSimpleName()},
                            locale));
        }
    }

    
    public void addShipToFleet(long fleetId, long shipId, Locale locale) {
            Fleet fleet = fleetRepository.findById(fleetId)
                    .orElseThrow(() -> new IllegalArgumentException(messageSource.getMessage(
                      "add_error_must_exist",
                            new Object[] {Fleet.class.getSimpleName(), Fleet.class.getSimpleName()},
                            locale)));
            Ship shipToAdd = shipRepository.findById(shipId)
                    .orElseThrow(() -> new IllegalArgumentException(messageSource.getMessage(
                      "add_error_must_exist",
                            new Object[] {Fleet.class.getSimpleName(), Ship.class.getSimpleName()},
                            locale)));
            if (!fleet.getShips().contains(shipToAdd)) {
                List<Ship> availableShips = shipRepository.findAvailableShipsByCountry(fleet.getCountry());
                if (!availableShips.contains(shipToAdd)) {
                    throw new IllegalArgumentException(messageSource.getMessage(
                                    "add_error_unavailable",
                                    new Object[]{Ship.class.getSimpleName(), Ship.class.getSimpleName()},
                                    locale));
                }
                shipToAdd.setFleet(fleet);
                shipRepository.save(shipToAdd);
            }
    }

    
    public void updateShipInFleet(long fleetId, long shipId, long newShipId, Locale locale) {
        if (shipId != newShipId) {
            Fleet fleet = fleetRepository.findById(fleetId)
                    .orElseThrow(() -> new IllegalArgumentException(messageSource.getMessage(
                            "update_error_must_exist",
                            new Object[]{Fleet.class.getSimpleName(), Fleet.class.getSimpleName()},
                            locale)));
            Ship shipToUpdate = shipRepository.findById(shipId)
                    .orElseThrow(() -> new IllegalArgumentException(messageSource.getMessage(
                            "update_error_must_exist",
                            new Object[]{Fleet.class.getSimpleName(), Ship.class.getSimpleName()},
                            locale)));
            if (!fleet.getShips().contains(shipToUpdate)) {
                throw new IllegalArgumentException(messageSource.getMessage(
                                "param0_has_no_such_param1",
                                new Object[]{Fleet.class.getSimpleName(), Ship.class.getSimpleName()},
                                locale));
            }
            removeShipFromFleet(fleetId, shipId, locale);
            addShipToFleet(fleetId, newShipId, locale);
        }
    }

    
    public void removeShipFromFleet(long fleetId, long shipId, Locale locale) {
            Ship shipToRemove = shipRepository.findById(shipId)
                    .orElseThrow(() -> new IllegalArgumentException(messageSource.getMessage(
                                    "delete_error_must_exist",
                                    new Object[] {Ship.class.getSimpleName()},
                                    locale)));
            shipToRemove.setFleet(null);
            shipRepository.save(shipToRemove);
    }

    public ShipDto findShipInFleet(long fleetId, long shipId, Locale locale) {
        return findShips(fleetId, locale).stream()
                .filter(shipDto -> shipDto.getId() == shipId)
                .findAny()
                .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage(
                        "search_error_not_found",
                        new Object[]{Ship.class.getSimpleName()},
                        locale))
                );
    }
}
