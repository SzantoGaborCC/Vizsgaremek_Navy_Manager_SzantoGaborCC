package com.codecool.navymanager.service;


import com.codecool.navymanager.dto.OfficerDto;
import com.codecool.navymanager.dto.ShipDto;
import com.codecool.navymanager.entity.Country;
import com.codecool.navymanager.entity.Officer;
import com.codecool.navymanager.entity.Ship;
import com.codecool.navymanager.entity.ShipClass;
import com.codecool.navymanager.repository.ShipClassRepository;
import com.codecool.navymanager.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

@Service

public class ShipService {
    @Autowired
    private MessageSource messageSource;

    private final ShipRepository shipRepository;

    private final OfficerService officerService;

    private final ShipClassRepository shipClassRepository;

    public ShipService(ShipRepository shipRepository, OfficerService officerService, ShipClassRepository shipClassRepository) {
        this.shipRepository = shipRepository;
        this.officerService = officerService;
        this.shipClassRepository = shipClassRepository;
    }

    public List<ShipDto> findAll() {
        return shipRepository.findAll().stream().map(ShipDto::new).toList();
    }

    public ShipDto findById(long id, Locale locale) {
        return new ShipDto(shipRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage(
                                "search_error_not_found",
                                new Object[] {Ship.class.getSimpleName()},
                                locale))));
    }

    public List<ShipDto> findAvailableShipsByCountryId(long id) {

        return shipRepository.findAvailableShipsByCountryId(id).stream()
                .map(ShipDto::new).toList();
    }


    public void add(ShipDto shipDto, Locale locale) {
        if (shipDto.getId() != null && shipRepository.existsById(shipDto.getId())) {
            throw new IllegalArgumentException(messageSource.getMessage(
                            "add_error_must_not_exist",
                            new Object[]{Ship.class.getSimpleName(), Ship.class.getSimpleName()},
                            locale));
        }
        ShipClass shipClass = shipClassRepository.findById(shipDto.getShipClass().getId())
                .orElseThrow(() -> new IllegalArgumentException(messageSource.getMessage(
                        "add_error_must_exist",
                        new Object[] {Ship.class.getSimpleName(), ShipClass.class.getSimpleName()},
                        locale)));
        if (!shipDto.getCountry().getId().equals(shipClass.getCountry().getId())) {
            throw new IllegalArgumentException(messageSource.getMessage(
                            "add_error_mismatch",
                            new Object[] {Ship.class.getSimpleName(), Country.class.getSimpleName()},
                            locale));
        }
        if (shipDto.getCaptain() != null && shipDto.getCaptain().getId() != -1) {
            OfficerDto officer = officerService.findById(shipDto.getCaptain().getId(), locale);
            if (!shipDto.getCountry().equals(officer.getCountry())) {
                throw new IllegalArgumentException(messageSource.getMessage(
                                "add_error_mismatch",
                                new Object[] {Ship.class.getSimpleName(), Country.class.getSimpleName()},
                                locale));
            }
            if (officerService.isOfficerPostedToShipOrFleet(officer)) {
                shipDto.setCaptain(null);
                throw new IllegalArgumentException(messageSource.getMessage(
                                "add_error_unavailable",
                                new Object[] {Ship.class.getSimpleName(), Officer.class.getSimpleName()},
                                locale));
            }
        } else if (shipDto.getCaptain() != null && shipDto.getCaptain().getId() == -1) {
            shipDto.setCaptain(null);
        }
        shipRepository.save(shipDto.toEntity());
    }


    public void update(ShipDto newShipData, long id, Locale locale) {
        if (newShipData.getId() == null || newShipData.getId() != id) {
            throw new IllegalArgumentException(messageSource.getMessage(
                            "update_error_id",
                            new Object[] {Ship.class.getSimpleName()},
                            locale));
        }
        Ship oldShipData = shipRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(messageSource.getMessage(
                                "update_error_must_exist",
                                new Object[] {Ship.class.getSimpleName(), Ship.class.getSimpleName()},
                                locale)));

        ShipClass shipClass = shipClassRepository.findById(newShipData.getShipClass().getId())
                .orElseThrow(() -> new IllegalArgumentException(messageSource.getMessage(
                        "update_error_must_exist",
                        new Object[] {Ship.class.getSimpleName(), ShipClass.class.getSimpleName()},
                        locale)));
        if (!newShipData.getCountry().getId().equals(shipClass.getCountry().getId())) {
            throw new IllegalArgumentException(messageSource.getMessage(
                            "update_error_mismatch",
                            new Object[] {Ship.class.getSimpleName(), Country.class.getSimpleName()},
                            locale));
        }

        checkOfficerEligibilityBasedOnShipData(newShipData, locale, oldShipData);

        Ship ship = newShipData.toEntity();
        ship.setFleet(oldShipData.getFleet());
        shipRepository.save(ship);
    }

    private void checkOfficerEligibilityBasedOnShipData(ShipDto newShipData, Locale locale, Ship oldShipData) {
        if (newShipData.getCaptain() != null && newShipData.getCaptain().getId() != -1) {
            OfficerDto newOfficer = officerService.findById(newShipData.getCaptain().getId(), locale);
            if (!newOfficer.getCountry().equals(newShipData.getCountry())) {
                throw new IllegalArgumentException(messageSource.getMessage(
                                "update_error_mismatch",
                                new Object[] {Ship.class.getSimpleName(), Country.class.getSimpleName()},
                                locale));
            }
            boolean isNewOfficerPosted = officerService.isOfficerPostedToShipOrFleet(newOfficer);
            if ((oldShipData.getCaptain() != null
                    && !oldShipData.getCaptain().getId().equals(newOfficer.getId())
                    && isNewOfficerPosted)
                    ||
                    (oldShipData.getCaptain() == null
                            && isNewOfficerPosted)
            ) {
                newShipData.setCaptain(null);
                throw new IllegalArgumentException(messageSource.getMessage(
                                "update_error_unavailable",
                                new Object[] {Ship.class.getSimpleName(), Officer.class.getSimpleName()},
                                locale));
            }
        } else {
            newShipData.setCaptain(null);
        }
    }

    public void deleteById(long id, Locale locale) {
        if (shipRepository.existsById(id)) {
            shipRepository.deleteById(id);
        } else {
            throw new NoSuchElementException(messageSource.getMessage(
                            "delete_error_must_exist",
                            new Object[] {Ship.class.getSimpleName()},
                            locale));
        }
    }
}
