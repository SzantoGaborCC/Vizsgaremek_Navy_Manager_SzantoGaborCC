package com.codecool.navymanager.service;



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
                .filter(gunAndQuantity -> gunAndQuantity.getId().equals(gunId)).findAny().orElseThrow());
    }

    @Transactional
    public void add(ShipClassDto shipClassDto) {
        shipClassRepository.save(shipClassDto.toEntity());
    }

    @Transactional
    public void update(ShipClassDto shipClassDto, long id) {
        if (shipClassRepository.existsById(id)) {
            shipClassRepository.save(shipClassDto.toEntity());
        } else {
            throw new IllegalArgumentException("No such Ship Class to update!");
        }
    }

    @Transactional
    public void deleteById(long id) {
        shipClassRepository.deleteById(id);
    }

    @Transactional
    public void addGunToShipClass(long shipClassId, GunAndQuantityDto gunAndQuantityDto) {
        try {
            ShipClass shipClass = shipClassRepository.findById(shipClassId).orElseThrow();
            shipClass.getGuns().add(gunAndQuantityDto.toEntity());
            shipClassRepository.save(shipClass);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid Ship or Gun Id!");
        }
    }

    @Transactional
    public void updateGunForAShipClass(
            long shipClassId,
            long gunId,
            GunAndQuantityDto newGunAndQuantityDto) {
        try {
            ShipClass shipClass = shipClassRepository.findById(shipClassId).orElseThrow();
            shipClass.getGuns().removeIf(gunAndQuantity -> gunAndQuantity.getId() == gunId);
            shipClass.getGuns().add(newGunAndQuantityDto.toEntity());
            shipClassRepository.save(shipClass);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid Ship or Gun Id!");
        }
    }

    @Transactional
    public void deleteGunFromShipClass(long shipClassId, long gunId) {
        ShipClass shipClass = shipClassRepository.findById(shipClassId).orElseThrow();
        shipClass.getGuns().removeIf(gunAndQuantity -> gunAndQuantity.getId() == gunId);
        shipClassRepository.save(shipClass);
    }
}
