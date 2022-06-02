package com.codecool.navymanager.service;



import com.codecool.navymanager.entity.GunAndQuantity;
import com.codecool.navymanager.entity.ShipClass;
import com.codecool.navymanager.entityDTO.GunAndQuantityDto;
import com.codecool.navymanager.entityDTO.GunDto;
import com.codecool.navymanager.entityDTO.RankDto;
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
        ShipClass shipClassToBeUpdated = shipClassRepository.findById(id).orElseThrow();
        ShipClass updatedShipClass = shipClassDto.toEntity();
        updatedShipClass.setGuns(shipClassToBeUpdated.getGuns());
        shipClassRepository.save(shipClassDto.toEntity());
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
        try {
            ShipClass shipClass = shipClassRepository.findById(shipClassId).orElseThrow();
            shipClass.getGuns().removeIf(gunAndQuantity -> gunAndQuantity.getGun().getId() == gunId);
            GunAndQuantity newGunAndQuantity = newGunAndQuantityDto.toEntity();
            newGunAndQuantity.setShipClass(shipClass);
            shipClass.getGuns().add(newGunAndQuantity);
            shipClassRepository.save(shipClass);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid Ship or Gun Id!" + e.getMessage());
        }
    }

    @Transactional
    public void deleteGunFromShipClass(long shipClassId, long gunId) {
        System.out.println("delete gun called on ship class!");
        ShipClass shipClass = shipClassRepository.findById(shipClassId).orElseThrow();
        System.out.println("before delete: " + shipClass.getGuns());
        if (shipClass.getGuns().removeIf(gunAndQuantity -> gunAndQuantity.getGun().getId() == gunId))
            System.out.println("removed!");
        System.out.println("after delete: " + shipClass.getGuns());
        shipClassRepository.save(shipClass);
        System.out.println("after rereading from database: " + shipClassRepository.findById(shipClassId).orElseThrow().getGuns());
    }
}
