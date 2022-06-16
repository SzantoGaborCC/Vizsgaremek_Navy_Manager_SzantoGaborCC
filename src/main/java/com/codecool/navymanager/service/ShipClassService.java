package com.codecool.navymanager.service;


import com.codecool.navymanager.entity.GunInstallation;
import com.codecool.navymanager.entity.ShipClass;
import com.codecool.navymanager.entityDTO.GunInstallationDto;
import com.codecool.navymanager.entityDTO.GunDto;
import com.codecool.navymanager.entityDTO.ShipClassDto;
import com.codecool.navymanager.repository.ShipClassRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ShipClassService {
    private final ShipClassRepository shipClassRepository;
    private final GunService gunService;

    public ShipClassService(ShipClassRepository shipClassRepository, GunService gunService) {
        this.shipClassRepository = shipClassRepository;
        this.gunService = gunService;
    }

    public List<ShipClassDto> findAll() {
        return shipClassRepository.findAll().stream().map(ShipClassDto::new).toList();
    }

    public ShipClassDto findById(long id) {
        return new ShipClassDto(shipClassRepository.findById(id).orElseThrow());
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
    public void update(ShipClassDto shipClassDto, long id) {
        ShipClass oldShipClassData = shipClassRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No such ship class!"));
        ShipClass newShipClassData = shipClassDto.toEntity();
        newShipClassData.setGuns(oldShipClassData.getGuns());
        shipClassRepository.save(newShipClassData);
    }

    @Transactional
    public void deleteById(long id) {
        shipClassRepository.deleteById(id);
    }

    @Transactional
    public void addGunToShipClass(long shipClassId, GunInstallationDto gunInstallationDto) {
            ShipClass shipClass = shipClassRepository.findById(shipClassId)
                    .orElseThrow(() -> new IllegalArgumentException("No such ship!"));
            GunInstallation oldGunInstallation = shipClass.getGuns().stream()
                    .filter(gunAndQuantity -> gunAndQuantity.getGun().getId().equals(gunInstallationDto.getGun().getId()))
                    .findAny().orElse(null);
            if (oldGunInstallation != null) {
                throw new IllegalArgumentException("That gun was already added to the ship class!");
            } else {
                GunInstallation gunInstallation = gunInstallationDto.toEntity();
                gunInstallation.setShipClass(shipClass);
                shipClass.getGuns().add(gunInstallation);
                shipClassRepository.save(shipClass);
            }
    }

    @Transactional
    public void updateGunForShipClass(
            long shipClassId,
            long gunId,
            GunInstallationDto newGunInstallationDto) {
        ShipClass shipClass = shipClassRepository.findById(shipClassId)
                .orElseThrow(() -> new IllegalArgumentException("No such ship class!"));
        GunInstallation gunInstallation = shipClass.getGuns().stream().filter(gQ -> gQ.getGun().getId() == gunId)
                .findAny().orElseThrow(() -> new IllegalArgumentException("Ship class has no such guns!"));
        gunInstallation.setGun(newGunInstallationDto.getGun().toEntity());
        gunInstallation.setGunQuantity(newGunInstallationDto.getQuantity());
        shipClassRepository.save(shipClass);
    }

    @Transactional
    public void removeGunFromShipClass(long shipClassId, long gunId) {
        ShipClass shipClass = shipClassRepository.findById(shipClassId)
                .orElseThrow(() -> new IllegalArgumentException("No such ship class!"));
        GunInstallation gunInstallation = shipClass.getGuns().stream().filter(gQ -> gQ.getGun().getId() == gunId)
                .findAny().orElseThrow(() -> new IllegalArgumentException("Ship class has no such guns!"));
        shipClass.getGuns().remove(gunInstallation);
        shipClassRepository.save(shipClass);
    }

    public List<GunDto> findValidGuns(ShipClassDto shipClassDto) {
        var gunIdsOfShipClass = shipClassRepository.findById(shipClassDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("No such ship class!"))
                .getGuns().stream()
                    .map(gunAndQuantity -> gunAndQuantity.getGun().getId()).toList();
        return gunService.findByCountry(shipClassDto.getCountry().getId()).stream()
                .filter(gunDto -> !gunIdsOfShipClass.contains(gunDto.getId()))
                .toList();
    }
}
