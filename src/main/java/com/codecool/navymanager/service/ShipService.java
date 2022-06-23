package com.codecool.navymanager.service;


import com.codecool.navymanager.dto.CountryDto;
import com.codecool.navymanager.dto.OfficerDto;
import com.codecool.navymanager.dto.ShipDto;
import com.codecool.navymanager.entity.Ship;
import com.codecool.navymanager.repository.CountryRepository;
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
    MessageSource messageSource;

    private final ShipRepository shipRepository;

    private final OfficerService officerService;
    private final CountryRepository countryRepository;

    private final ShipClassRepository shipClassRepository;

    public ShipService(ShipRepository shipRepository, OfficerService officerService, CountryRepository countryRepository, ShipClassRepository shipClassRepository) {
        this.shipRepository = shipRepository;
        this.officerService = officerService;
        this.countryRepository = countryRepository;
        this.shipClassRepository = shipClassRepository;
    }

    public List<ShipDto> findAll() {
        return shipRepository.findAll().stream().map(ShipDto::new).toList();
    }

    public ShipDto findById(long id, Locale locale) {
        return new ShipDto(shipRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Ship Search Error: Ship is not in the database!")));
    }

    public List<ShipDto> findByCountry(CountryDto countryDto) {
        return shipRepository.findByCountry(countryDto.toEntity()).stream()
                .map(ShipDto::new).toList();
    }

    public List<ShipDto> findAvailableShipsByCountry(CountryDto countryDto) {
        return shipRepository.findAvailableShipsByCountry(countryDto.toEntity()).stream()
                .map(ShipDto::new).toList();
    }


    public void add(ShipDto shipDto, Locale locale) {
        if (shipRepository.existsById(shipDto.getId()))
            throw new IllegalArgumentException("Add Ship Error: Ship already exists!");
        if (shipDto.getCaptain() != null && shipDto.getCaptain().getId() != -1) {
            OfficerDto officer = officerService.findById(shipDto.getCaptain().getId(), locale);
            if (!shipDto.getCountry().equals(officer.getCountry())) {
                throw new IllegalArgumentException("Add Ship Error: Country mismatch!");
            }
            if (officerService.isOfficerPostedToShipOrFleet(officer)) {
                shipDto.setCaptain(null);
                throw new IllegalArgumentException("Add Ship Error: Officer is unavailable!");
            }
        } else if (shipDto.getCaptain() != null && shipDto.getCaptain().getId() == -1) {
            shipDto.setCaptain(null);
        }
        shipRepository.save(shipDto.toEntity());
    }


    public void update(ShipDto newShipData, long id, Locale locale) {
        if (newShipData.getId() == null || newShipData.getId() != id) {
            throw new IllegalArgumentException("Ship Update error: Id cannot be null, and must match Id in the path!");
        }
        Ship oldShipData = shipRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ship Update error: Ship must already exist in the database!"));

        checkOfficerEligibilityBasedOnShipData(newShipData, locale, oldShipData);

        Ship ship = newShipData.toEntity();
        ship.setFleet(oldShipData.getFleet());
        shipRepository.save(ship);
    }

    private void checkOfficerEligibilityBasedOnShipData(ShipDto newShipData, Locale locale, Ship oldShipData) {
        if (newShipData.getCaptain() != null && newShipData.getCaptain().getId() != -1) {
            OfficerDto newOfficer = officerService.findById(newShipData.getCaptain().getId(), locale);
            if (!newOfficer.getCountry().equals(newShipData.getCountry())) {
                throw new IllegalArgumentException("Fleet Update Error: Country Mismatch!");
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
                throw new IllegalArgumentException("Ship Update Error: Officer is not available!");
            }
        } else {
            newShipData.setCaptain(null);
        }
    }

    public void deleteById(long id, Locale locale) {
        if (shipRepository.existsById(id)) {
            shipRepository.deleteById(id);
        } else {
            throw new NoSuchElementException("Delete Ship Error : Ship must already exist in the database!");
        }
    }
}
