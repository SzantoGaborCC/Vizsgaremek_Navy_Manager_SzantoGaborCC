package com.codecool.navymanager.service;


import com.codecool.navymanager.entity.GunAndQuantity;
import com.codecool.navymanager.entity.ShipClass;
import com.codecool.navymanager.entityDTO.GunAndQuantityDto;
import com.codecool.navymanager.entityDTO.ShipClassDto;
import com.codecool.navymanager.repository.ShipClassRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ShipClassService {
    private final ShipClassRepository shipClassRepository;

    public ShipClassService(ShipClassRepository shipClassRepository) {
        this.shipClassRepository = shipClassRepository;
    }

    public List<ShipClassDto> findAll() {
        return shipClassRepository.findAll().stream().map(ShipClassDto::new).toList();
    }

    public ShipClassDto findById(long id) {
        return new ShipClassDto(shipClassRepository.findById(id).orElseThrow());
    }

    public GunAndQuantityDto findGunAndQuantityByShipClassIdAndGunId(long shipClassId, long gunId) {
        return new GunAndQuantityDto(shipClassRepository.findById(shipClassId).orElseThrow().getGuns().stream()
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
    public void addGunToShipClass(long shipClassId, GunAndQuantityDto gunAndQuantityDto) {
        try {
            ShipClass shipClass = shipClassRepository.findById(shipClassId).orElseThrow();
            GunAndQuantity gunAndQuantity = gunAndQuantityDto.toEntity();
            gunAndQuantity.setShipClass(shipClass);
            shipClass.getGuns().add(gunAndQuantity);
            shipClassRepository.save(shipClass);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid Ship or Gun Id! " + e.getMessage());
        }
    }

    @Transactional
    public void updateGunForAShipClass(
            long shipClassId,
            long gunId,
            GunAndQuantityDto newGunAndQuantityDto) {
        ShipClass shipClass = shipClassRepository.findById(shipClassId)
                .orElseThrow(() -> new IllegalArgumentException("No such ship class!"));
        GunAndQuantity gunAndQuantity = shipClass.getGuns().stream().filter(gQ -> gQ.getGun().getId() == gunId)
                .findAny().orElseThrow(() -> new IllegalArgumentException("Ship class has no such guns!"));
        gunAndQuantity.setGun(newGunAndQuantityDto.getGun().toEntity());
        gunAndQuantity.setGunQuantity(newGunAndQuantityDto.getQuantity());
        shipClassRepository.save(shipClass);
    }

    @Transactional
    public void removeGunFromShipClass(long shipClassId, long gunId) {
        ShipClass shipClass = shipClassRepository.findById(shipClassId)
                .orElseThrow(() -> new IllegalArgumentException("No such ship class!"));
        GunAndQuantity gunAndQuantity = shipClass.getGuns().stream().filter(gQ -> gQ.getGun().getId() == gunId)
                .findAny().orElseThrow(() -> new IllegalArgumentException("Ship class has no such guns!"));
        shipClass.getGuns().remove(gunAndQuantity);
        shipClassRepository.save(shipClass);
    }
}
