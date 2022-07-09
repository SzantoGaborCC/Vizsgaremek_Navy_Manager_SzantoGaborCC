package com.codecool.navymanager.service;

import com.codecool.navymanager.dto.OfficerDto;
import com.codecool.navymanager.entity.Fleet;
import com.codecool.navymanager.entity.Officer;
import com.codecool.navymanager.entity.Ship;
import com.codecool.navymanager.repository.FleetRepository;
import com.codecool.navymanager.repository.OfficerRepository;
import com.codecool.navymanager.repository.ShipRepository;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

@Service

public class OfficerService {
    private final MessageSource messageSource;
    private final OfficerRepository officerRepository;

    private final ShipRepository shipRepository;

    private final FleetRepository fleetRepository;

    public OfficerService(MessageSource messageSource, OfficerRepository officerRepository, ShipRepository shipRepository, FleetRepository fleetRepository) {
        this.messageSource = messageSource;
        this.officerRepository = officerRepository;
        this.shipRepository = shipRepository;
        this.fleetRepository = fleetRepository;
    }

    public List<OfficerDto> findAll() {
        return officerRepository.findAll().stream().map(OfficerDto::new).toList();
    }

    public OfficerDto findById(long id, Locale locale) {
        return new OfficerDto(officerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage(
                        "no_such",
                        new Object[] {Officer.class.getSimpleName()},
                        locale))));
    }

    public List<OfficerDto> findAvailableOfficers() {
        return officerRepository.findAvailableOfficers().stream()
                .map(OfficerDto::new).toList();
    }

    public List<OfficerDto> findAvailableOfficersByCountry(long countryId) {
        return officerRepository.findAvailableOfficersByCountry(countryId).stream()
                .map(OfficerDto::new).toList();
    }

    public List<OfficerDto> findAvailableOfficersForShip(long shipId, Locale locale) {
        List<OfficerDto> foundOfficers = new ArrayList<>();
        Ship ship = shipRepository.findById(shipId)
                .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage(
                        "no_such",
                        new Object[] {Fleet.class.getSimpleName()},
                        locale)));
        if (ship.getCaptain() != null) {
            foundOfficers.add(new OfficerDto(ship.getCaptain()));
        }
        foundOfficers.addAll(officerRepository.findAvailableOfficers().stream()
                .map(OfficerDto::new).toList());
        return foundOfficers;
    }

    public List<OfficerDto> findAvailableOfficersForFleet(long fleetId, Locale locale) {
        List<OfficerDto> foundOfficers = new ArrayList<>();
        Fleet fleet = fleetRepository.findById(fleetId)
                .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage(
                        "no_such",
                        new Object[] {Fleet.class.getSimpleName()},
                        locale)));
        if (fleet.getCommander() != null) {
            foundOfficers.add(new OfficerDto(fleet.getCommander()));
        }
        foundOfficers.addAll(officerRepository.findAvailableOfficers().stream()
                .map(OfficerDto::new)
                .toList());
        return foundOfficers;
    }

    public boolean isOfficerPostedToShipOrFleet(OfficerDto officer) {
        return officer != null &&
                findAvailableOfficersByCountry(officer.getCountry().getId()).stream()
                        .noneMatch(officerDto -> officerDto.getId().equals(officer.getId()));
    }

    public void add(OfficerDto officerDto, Locale locale) {
        if (officerDto.getId() != null && officerRepository.existsById(officerDto.getId())) {
            throw new IllegalArgumentException(messageSource.getMessage(
                    "add_error_must_not_exist",
                    new Object[]{Officer.class.getSimpleName(), Officer.class.getSimpleName()},
                    locale));
        }
        officerRepository.save(officerDto.toEntity());
    }

    
    public void update(OfficerDto officerDto, long id, Locale locale) {
        if (officerDto.getId() == null || officerDto.getId() != id) {
            throw new IllegalArgumentException(messageSource.getMessage(
                            "update_error_id",
                            new Object[] {Officer.class.getSimpleName()},
                            locale));
        }
        if (officerRepository.existsById(id)) {
            officerRepository.save(officerDto.toEntity());
        } else {
            throw new IllegalArgumentException(messageSource.getMessage(
                            "update_error_must_exist",
                            new Object[] {Officer.class.getSimpleName(), Officer.class.getSimpleName()},
                            locale));

        }
    }
    
    public void deleteById(long id, Locale locale) {
        if (officerRepository.existsById(id)) {
            officerRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException(messageSource.getMessage(
                            "delete_error_must_exist",
                            new Object[] {Officer.class.getSimpleName()},
                            locale));
        }
    }
}
