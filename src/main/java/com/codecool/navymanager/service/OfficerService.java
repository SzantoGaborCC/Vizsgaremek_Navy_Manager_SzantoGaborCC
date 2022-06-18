package com.codecool.navymanager.service;

import com.codecool.navymanager.dto.FleetDto;
import com.codecool.navymanager.dto.OfficerDto;
import com.codecool.navymanager.dto.ShipDto;
import com.codecool.navymanager.entity.Country;
import com.codecool.navymanager.entity.Officer;
import com.codecool.navymanager.repository.OfficerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class OfficerService {
    @Autowired
    MessageSource messageSource;
    private final OfficerRepository officerRepository;

    public OfficerService(OfficerRepository officerRepository) {
        this.officerRepository = officerRepository;
    }

    public List<OfficerDto> findAll() {
        return officerRepository.findAll().stream().map(OfficerDto::new).toList();
    }

    public OfficerDto findById(long id, Locale locale) {
        return new OfficerDto(officerRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException(messageSource.getMessage(
                        "no_such",
                        new Object[] {Officer.class.getSimpleName()},
                        locale))
        ));
    }

    public List<OfficerDto> findAvailableOfficersForShip(ShipDto shipDto)  {
        List<OfficerDto> foundOfficers = new ArrayList<>();
        foundOfficers.add(OfficerDto.UNASSIGNED_OFFICER);
        if (shipDto.getCaptain() != null) {
            foundOfficers.add(shipDto.getCaptain());
        }
        foundOfficers.addAll(officerRepository.findAvailableOfficersByCountry(shipDto.getCountry().toEntity()).stream()
                .filter(officer -> officer.getRank().getPrecedence() >=
                        shipDto.getShipClass().getHullClassification().getMinimumRank().getPrecedence())
                .map(OfficerDto::new).toList());
        return foundOfficers;
    }

    public List<OfficerDto> findAvailableOfficersForFleet(FleetDto fleetDto)  {
        List<OfficerDto> foundOfficers = new ArrayList<>();
        foundOfficers.add(OfficerDto.UNASSIGNED_OFFICER);
        if (fleetDto.getCommander() != null) {
            foundOfficers.add(fleetDto.getCommander());
        }
        foundOfficers.addAll(officerRepository.findAvailableOfficersByCountry(fleetDto.getCountry().toEntity()).stream()
                .filter(officer -> officer.getRank().getPrecedence() >= fleetDto.getMinimumRank().getPrecedence())
                .map(OfficerDto::new)
                .toList());
        return foundOfficers;
    }

    @Transactional
    public void add(OfficerDto officerDto) {
        officerRepository.save(officerDto.toEntity());
    }

    @Transactional
    public void update(OfficerDto officerDto, long id, Locale locale) {
        if (officerRepository.existsById(id)) {
            officerRepository.save(officerDto.toEntity());
        } else {
            throw new NoSuchElementException(messageSource.getMessage(
                    "no_such",
                    new Object[] {Officer.class.getSimpleName()},
                    locale));
        }
    }

    @Transactional
    public void deleteById(long id, Locale locale) {
        if (officerRepository.existsById(id)) {
            officerRepository.deleteById(id);
        } else {
            throw  new NoSuchElementException(
                    messageSource.getMessage(
                            "no_such",
                            new Object[] {Officer.class.getSimpleName()},
                            locale));
        }
    }
}
