package com.codecool.navymanager.service;

import com.codecool.navymanager.dto.CountryDto;
import com.codecool.navymanager.dto.FleetDto;
import com.codecool.navymanager.dto.OfficerDto;
import com.codecool.navymanager.dto.ShipDto;
import com.codecool.navymanager.entity.*;
import com.codecool.navymanager.repository.FleetRepository;
import com.codecool.navymanager.repository.OfficerRepository;
import com.codecool.navymanager.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

@Service

public class OfficerService {
    @Autowired
    private MessageSource messageSource;
    private final OfficerRepository officerRepository;
    private final FleetRepository fleetRepository;
    private final ShipRepository shipRepository;

    public OfficerService(OfficerRepository officerRepository, FleetRepository fleetRepository, ShipRepository shipRepository) {
        this.officerRepository = officerRepository;
        this.fleetRepository = fleetRepository;
        this.shipRepository = shipRepository;
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

    public List<OfficerDto> findAvailableOfficersByCountry(CountryDto country) {
        return officerRepository.findAvailableOfficersByCountry(country.toEntity()).stream()
                .map(OfficerDto::new).toList();
    }

    public List<OfficerDto> findAvailableOfficersForShip(ShipDto shipDto) {
        List<OfficerDto> foundOfficers = new ArrayList<>();
        if (shipDto.getCaptain() != null) {
            foundOfficers.add(shipDto.getCaptain());
        }
        foundOfficers.addAll(officerRepository.findAvailableOfficers().stream()
                .map(OfficerDto::new).toList());
        return foundOfficers;
    }

    public List<OfficerDto> findAvailableOfficersForFleet(FleetDto fleetDto) {
        List<OfficerDto> foundOfficers = new ArrayList<>();
        if (fleetDto.getCommander() != null) {
            foundOfficers.add(fleetDto.getCommander());
        }
        foundOfficers.addAll(officerRepository.findAvailableOfficers().stream()
                .map(OfficerDto::new)
                .toList());
        return foundOfficers;
    }

    public boolean isOfficerPostedToShipOrFleet(OfficerDto officer) {
        return officer != null &&
                findAvailableOfficersByCountry(officer.getCountry()).stream()
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
