package com.codecool.navymanager.service;


import com.codecool.navymanager.dto.CountryDto;
import com.codecool.navymanager.dto.OfficerDto;
import com.codecool.navymanager.dto.ShipDto;
import com.codecool.navymanager.entity.Officer;
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

    
    public void add(ShipDto shipDto, Locale locale) {
        if (shipDto.getCaptain() != null && shipDto.getCaptain().getId() != -1) {
            OfficerDto officer = officerService.findById(shipDto.getCaptain().getId(), locale);
            if (isOfficerPosted(officer)) {
                shipDto.setCaptain(null);
                throw new IllegalArgumentException(messageSource.getMessage(
                        "invalid_data",
                        new Object[]{Officer.class.getSimpleName()},
                        locale));
            }
        } else if (shipDto.getCaptain().getId() == -1) {
            shipDto.setCaptain(null);
        }
        shipRepository.save(shipDto.toEntity());
    }

    
    public void update(ShipDto shipDto, long id, Locale locale) {
        if (shipDto.getId() == null || shipDto.getId() != id) {
            throw new IllegalArgumentException("Ship Update error: Id cannot be null, and must match Id in the path!");
        }
        Ship oldShipData = shipRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ship Update error: No such ship in the database!"));
        OfficerDto officer = (shipDto.getCaptain() != null && shipDto.getCaptain().getId() != -1) ? officerService.findById(shipDto.getCaptain().getId(), locale) : null;
        if (oldShipData.getCaptain() == null && isOfficerPosted(officer)) {
            shipDto.setCaptain(null);
            throw new IllegalArgumentException("Ship Update Error: Officer is not available!");
        }
        Ship newShipData = shipDto.toEntity();
        newShipData.setId(id);
        newShipData.setFleet(oldShipData.getFleet());
        shipRepository.save(newShipData);
    }

    public boolean isOfficerPosted(OfficerDto officer) {
        return officer != null &&
                (
                        officer.getId() == -1 ||
                                officerService.findAvailableOfficersByCountry(officer.getCountry()).stream()
                                        .noneMatch(officerDto -> officerDto.getId().equals(officer.getId())));
    }


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
