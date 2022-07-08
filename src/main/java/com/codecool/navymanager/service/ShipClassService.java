package com.codecool.navymanager.service;


import com.codecool.navymanager.dto.GunDto;
import com.codecool.navymanager.dto.GunInstallationDto;
import com.codecool.navymanager.dto.ShipClassDto;
import com.codecool.navymanager.entity.*;
import com.codecool.navymanager.repository.GunInstallationRepository;
import com.codecool.navymanager.repository.ShipClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

@Service

public class ShipClassService {

    @Autowired
    private MessageSource messageSource;
    private final ShipClassRepository shipClassRepository;
    private final GunService gunService;
    private final GunInstallationRepository gunInstallationRepository;

    public ShipClassService(ShipClassRepository shipClassRepository, GunService gunService, GunInstallationRepository gunInstallationRepository) {
        this.shipClassRepository = shipClassRepository;
        this.gunService = gunService;
        this.gunInstallationRepository = gunInstallationRepository;
    }

    public List<ShipClassDto> findAll() {
        return shipClassRepository.findAll().stream().map(ShipClassDto::new).toList();
    }

    public ShipClassDto findById(long id, Locale locale) {
        return new ShipClassDto(shipClassRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage(
                        "no_such",
                        new Object[]{ShipClass.class.getSimpleName()},
                        locale))));
    }

    public GunInstallationDto findGunInstallationByShipClassIdAndGunId(long shipClassId, long gunId) {
        return new GunInstallationDto(shipClassRepository.findById(shipClassId).orElseThrow().getGuns().stream()
                .filter(gunAndQuantity -> gunAndQuantity.getGun().getId().equals(gunId)).findAny().orElseThrow());
    }


    public void add(ShipClassDto shipClassDto, Locale locale) {
        if (shipClassDto.getId() != null && shipClassRepository.existsById(shipClassDto.getId()))
            throw new IllegalArgumentException(messageSource.getMessage(
                    "add_error_must_not_exist",
                    new Object[]{ShipClass.class.getSimpleName(), ShipClass.class.getSimpleName()},
                    locale));
        shipClassRepository.save(shipClassDto.toEntity());
    }

@Transactional
    public void update(ShipClassDto newShipClassData, long id, Locale locale) {
        if (newShipClassData.getId() == null || newShipClassData.getId() != id) {
            throw new IllegalArgumentException(messageSource.getMessage(
                    "update_error_id",
                    new Object[]{ShipClass.class.getSimpleName()},
                    locale));
        }
        ShipClass oldShipClassData = shipClassRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(messageSource.getMessage(
                        "update_error_must_exist",
                        new Object[]{ShipClass.class.getSimpleName(), ShipClass.class.getSimpleName()},
                        locale)));
        ShipClass newShipClass = newShipClassData.toEntity();
        if (!newShipClassData.getCountry().getId().equals(oldShipClassData.getCountry().getId())) {
            gunInstallationRepository.deleteByShipClass(oldShipClassData);
        }
        shipClassRepository.save(newShipClass);
    }

    public void deleteById(long id, Locale locale) {
        if (shipClassRepository.existsById(id)) {
            shipClassRepository.deleteById(id);
        } else {
            throw new NoSuchElementException(messageSource.getMessage(
                    "delete_error_must_exist",
                    new Object[]{ShipClass.class.getSimpleName()},
                    locale));
        }
    }


    public void addGunToShipClass(long shipClassId, GunInstallationDto gunInstallationDto, Locale locale) {
        ShipClass shipClass = shipClassRepository.findById(shipClassId)
                .orElseThrow(() -> new IllegalArgumentException(messageSource.getMessage(
                        "add_error_must_exist",
                        new Object[]{Gun.class.getSimpleName(), ShipClass.class.getSimpleName()},
                        locale)));
        GunDto newGunDto = gunService.findById(gunInstallationDto.getGun().getId(), locale);
        if (!shipClass.getCountry().getId().equals(newGunDto.getCountry().getId())) {
            throw new IllegalArgumentException(messageSource.getMessage(
                    "add_error_mismatch",
                    new Object[]{Gun.class.getSimpleName(), Country.class.getSimpleName()},
                    locale));
        }
        if (shipClass.getGuns().stream()
                .noneMatch(gunInstallation ->
                        gunInstallation.getGun().getId().equals(gunInstallationDto.getGun().getId()))) {
            GunInstallation gunInstallation = gunInstallationDto.toEntity();
            gunInstallation.setShipClass(shipClass);
            shipClass.getGuns().add(gunInstallation);
            shipClassRepository.save(shipClass);
        } else {
            throw new IllegalArgumentException(messageSource.getMessage(
                    "add_error_must_not_exist",
                    new Object[]{Gun.class.getSimpleName(), Gun.class.getSimpleName()},
                    locale));
        }
    }

    public void updateGunForShipClass(
            long shipClassId,
            long gunId,
            GunInstallationDto newGunInstallationDto,
            Locale locale) {
        ShipClass shipClass = shipClassRepository.findById(shipClassId)
                .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage(
                        "no_such",
                        new Object[]{Ship.class.getSimpleName()},
                        locale)));
        GunInstallation oldGunInstallation = shipClass.getGuns().stream().filter(gInst -> gInst.getGun().getId() == gunId)
                .findAny().orElseThrow(() -> new NoSuchElementException(messageSource.getMessage(
                        "param0_has_no_such_param1",
                        new Object[]{ShipClass.class.getSimpleName(), Gun.class.getSimpleName()},
                        locale)));
        GunDto newGunDto = gunService.findById(newGunInstallationDto.getGun().getId(), locale);
        if (!shipClass.getCountry().getId().equals(newGunDto.getCountry().getId())) {
            throw new IllegalArgumentException(messageSource.getMessage(
                    "update_error_mismatch",
                    new Object[]{Gun.class.getSimpleName(), Country.class.getSimpleName()},
                    locale));
        }
        oldGunInstallation.setGun(newGunInstallationDto.getGun().toEntity());
        oldGunInstallation.setGunQuantity(newGunInstallationDto.getQuantity());
        shipClassRepository.save(shipClass);
    }


    public void removeGunFromShipClass(long shipClassId, long gunId, Locale locale) {
        ShipClass shipClass = shipClassRepository.findById(shipClassId)
                .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage(
                        "no_such",
                        new Object[]{ShipClass.class.getSimpleName()},
                        locale)));
        GunInstallation gunInstallation = shipClass.getGuns().stream().filter(gInst -> gInst.getGun().getId() == gunId)
                .findAny().orElseThrow(() -> new NoSuchElementException(messageSource.getMessage(
                        "delete_error_must_exist",
                        new Object[]{Gun.class.getSimpleName()},
                        locale)));
        shipClass.getGuns().remove(gunInstallation);
        gunInstallationRepository.deleteById(gunInstallation.getId());
        shipClassRepository.save(shipClass);
    }

    public List<GunDto> findValidGuns(long id, Locale locale) {
        ShipClass shipClass = shipClassRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage(
                        "no_such",
                        new Object[]{ShipClass.class.getSimpleName()},
                        locale)));
        List<Long> gunIdsOfShipClass = shipClass.getGuns().stream()
                .map(gunAndQuantity -> gunAndQuantity.getGun().getId()).toList();
        return gunService.findByCountry(shipClass.getCountry().getId()).stream()
                .filter(gunDto -> !gunIdsOfShipClass.contains(gunDto.getId()))
                .toList();
    }

    public List<GunInstallationDto> findGuns(long id, Locale locale) {
        return findById(id, locale).getGuns().stream().toList();
    }

    public GunInstallationDto findGunInShipClassById(long shipClassId, long gunId, Locale locale) {
        return findGuns(shipClassId, locale).stream()
                .filter(gunInstallationDto -> gunInstallationDto.getGun().getId() == gunId)
                .findAny()
                .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage(
                        "search_error_not_found",
                        new Object[]{Gun.class.getSimpleName()},
                        locale))
                );
    }
}
