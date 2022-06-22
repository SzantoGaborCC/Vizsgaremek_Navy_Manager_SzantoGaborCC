package com.codecool.navymanager.service;


import com.codecool.navymanager.dto.CountryDto;
import com.codecool.navymanager.dto.OfficerDto;
import com.codecool.navymanager.dto.ShipDto;
import com.codecool.navymanager.entity.Country;
import com.codecool.navymanager.entity.Officer;
import com.codecool.navymanager.entity.Ship;
import com.codecool.navymanager.repository.CountryRepository;
import com.codecool.navymanager.repository.OfficerRepository;
import com.codecool.navymanager.repository.ShipClassRepository;
import com.codecool.navymanager.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class ShipService {
    @Autowired
    MessageSource messageSource;

    private final ShipRepository shipRepository;
    private final OfficerRepository officerRepository;

    private final CountryRepository countryRepository;

    private final ShipClassRepository shipClassRepository;

    public ShipService(ShipRepository shipRepository, OfficerRepository officerRepository, CountryRepository countryRepository, ShipClassRepository shipClassRepository) {
        this.shipRepository = shipRepository;
        this.officerRepository = officerRepository;
        this.countryRepository = countryRepository;
        this.shipClassRepository = shipClassRepository;
    }

    public List<ShipDto> findAll() {
        return shipRepository.findAll().stream().map(ShipDto::new).toList();
    }

    public ShipDto findById(long id, Locale locale) {
        return new ShipDto(shipRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage(
                        "no_such",
                        new Object[]{Ship.class.getSimpleName()},
                        locale))));
    }

    public List<ShipDto> findByCountry(CountryDto countryDto) {
        return shipRepository.findByCountry(countryDto.toEntity()).stream()
                .map(ShipDto::new).toList();
    }

    public List<ShipDto> findAvailableShipsByCountry(CountryDto countryDto) {
        return shipRepository.findAvailableShipsByCountry(countryDto.toEntity()).stream()
                .map(ShipDto::new).toList();
    }

    @Transactional
    public void add(ShipDto shipDto, Locale locale) {
        //checkIfOfficerEligible(shipDto, locale);
        if (shipDto.getCaptain().getId() == -1)
            shipDto.setCaptain(null);
        shipRepository.save(shipDto.toEntity());
    }

    private void checkIfOfficerEligible(ShipDto shipDto, Locale locale) {
        if (shipDto.getCaptain() != null) {
            Officer officer = shipDto.getCaptain().toEntity();
            Officer oldOfficer = officerRepository.findById(officer.getId())
                    .orElseThrow(() -> new IllegalArgumentException(messageSource.getMessage(
                            "no_such",
                            new Object[]{Ship.class.getSimpleName()},
                            locale)));
            if (
                    (!officer.equals(oldOfficer)) &&
                            (officerRepository.findFleetPost(officer) != null || officerRepository.findShipPost(officer) != null) &&
                            officer.getRank().getPrecedence() < shipDto.getShipClass().getHullClassification().getMinimumRank().getPrecedence()
            ) {
                throw new IllegalArgumentException(messageSource.getMessage(
                        "invalid_data",
                        new Object[]{Officer.class.getSimpleName()},
                        locale));
            }
        }
    }

    @Transactional
    public void update(ShipDto shipDto, long id, Locale locale) {
        Ship oldShipData = shipRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(messageSource.getMessage(
                        "no_such",
                        new Object[]{Ship.class.getSimpleName()},
                        locale)));
        //checkIfOfficerEligible(shipDto, locale);
        Ship newShipData = shipDto.toEntity();
        newShipData.setFleet(oldShipData.getFleet());
        if (newShipData.getCaptain().getId() == -1)
            newShipData.setCaptain(null);
        shipRepository.save(newShipData);
    }

    @Transactional
    public void deleteById(long id, Locale locale) {
        if (shipRepository.existsById(id)) {
            shipRepository.deleteById(id);
        } else {
            throw new NoSuchElementException(
                    messageSource.getMessage(
                            "no_such",
                            new Object[]{Ship.class.getSimpleName()},
                            locale));
        }
    }
}
