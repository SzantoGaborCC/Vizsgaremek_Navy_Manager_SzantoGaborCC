package com.codecool.navymanager.service;


import com.codecool.navymanager.entity.*;
import com.codecool.navymanager.dto.GunInstallationDto;
import com.codecool.navymanager.dto.GunDto;
import com.codecool.navymanager.dto.ShipClassDto;
import com.codecool.navymanager.repository.ShipClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class ShipClassService {

    @Autowired
    MessageSource messageSource;
    private final ShipClassRepository shipClassRepository;
    private final GunService gunService;

    public ShipClassService(ShipClassRepository shipClassRepository, GunService gunService) {
        this.shipClassRepository = shipClassRepository;
        this.gunService = gunService;
    }

    public List<ShipClassDto> findAll() {
        return shipClassRepository.findAll().stream().map(ShipClassDto::new).toList();
    }

    public ShipClassDto findById(long id, Locale locale) {
        return new ShipClassDto(shipClassRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage(
                        "no_such",
                        new Object[] {ShipClass.class.getSimpleName()},
                        locale))));
    }

    public GunInstallationDto findGunAndQuantityByShipClassIdAndGunId(long shipClassId, long gunId) {
        return new GunInstallationDto(shipClassRepository.findById(shipClassId).orElseThrow().getGuns().stream()
                .filter(gunAndQuantity -> gunAndQuantity.getGun().getId().equals(gunId)).findAny().orElseThrow());
    }

    @Transactional
    public void add(ShipClassDto shipClassDto) {
        shipClassRepository.save(shipClassDto.toEntity());
    }

    @Transactional
    public void update(ShipClassDto shipClassDto, long id, Locale locale) {
        ShipClass oldShipClassData = shipClassRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException( messageSource.getMessage(
                        "no_such",
                        new Object[] {ShipClass.class.getSimpleName()},
                        locale)));
        ShipClass newShipClassData = shipClassDto.toEntity();
        newShipClassData.setGuns(oldShipClassData.getGuns());
        shipClassRepository.save(newShipClassData);
    }

    @Transactional
    public void deleteById(long id, Locale locale) {
        if (shipClassRepository.existsById(id)) {
            shipClassRepository.deleteById(id);
        } else {
            throw  new NoSuchElementException(
                    messageSource.getMessage(
                            "no_such",
                            new Object[] {ShipClass.class.getSimpleName()},
                            locale));
        }
    }

    @Transactional
    public void addGunToShipClass(long shipClassId, GunInstallationDto gunInstallationDto, Locale locale) {
            ShipClass shipClass = shipClassRepository.findById(shipClassId)
                    .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage(
                            "no_such",
                            new Object[] {ShipClass.class.getSimpleName()},
                            locale)));
            if (shipClass.getGuns().stream()
                    .noneMatch(gunInstallation ->
                            gunInstallation.getGun().getId().equals(gunInstallationDto.getGun().getId()))) {
                GunInstallation gunInstallation = gunInstallationDto.toEntity();
                gunInstallation.setShipClass(shipClass);
                shipClass.getGuns().add(gunInstallation);
                shipClassRepository.save(shipClass);
            } else {
                throw new IllegalArgumentException(messageSource.getMessage(
                        "invalid_data",
                        new Object[] {Gun.class.getSimpleName()},
                        locale));
            }
    }

    @Transactional
    public void updateGunForShipClass(
            long shipClassId,
            long gunId,
            GunInstallationDto newGunInstallationDto,
            Locale locale) {
        ShipClass shipClass = shipClassRepository.findById(shipClassId)
                .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage(
                        "no_such",
                        new Object[] {Ship.class.getSimpleName()},
                        locale)));
        GunInstallation gunInstallation = shipClass.getGuns().stream().filter(gInst -> gInst.getGun().getId() == gunId)
                .findAny().orElseThrow(() -> new NoSuchElementException(messageSource.getMessage(
                        "param0_has_no_such_param1",
                        new Object[] {ShipClass.class.getSimpleName(), Gun.class.getSimpleName()},
                        locale)));
        gunInstallation.setGun(newGunInstallationDto.getGun().toEntity());
        gunInstallation.setGunQuantity(newGunInstallationDto.getQuantity());
        shipClassRepository.save(shipClass);
    }

    @Transactional
    public void removeGunFromShipClass(long shipClassId, long gunId, Locale locale) {
        ShipClass shipClass = shipClassRepository.findById(shipClassId)
                .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage(
                        "no_such",
                        new Object[] {ShipClass.class.getSimpleName()},
                        locale)));
        GunInstallation gunInstallation = shipClass.getGuns().stream().filter(gInst -> gInst.getGun().getId() == gunId)
                .findAny().orElseThrow(() -> new NoSuchElementException(messageSource.getMessage(
                        "no_such",
                        new Object[] {Gun.class.getSimpleName()},
                        locale)));
        shipClass.getGuns().remove(gunInstallation);
        shipClassRepository.save(shipClass);
    }

    public List<GunDto> findValidGuns(ShipClassDto shipClassDto, Locale locale) {
        var gunIdsOfShipClass = shipClassRepository.findById(shipClassDto.getId())
                .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage(
                        "no_such",
                        new Object[] {ShipClass.class.getSimpleName()},
                        locale)))
                .getGuns().stream()
                    .map(gunAndQuantity -> gunAndQuantity.getGun().getId()).toList();
        return gunService.findByCountry(shipClassDto.getCountry().getId()).stream()
                .filter(gunDto -> !gunIdsOfShipClass.contains(gunDto.getId()))
                .toList();
    }
}
