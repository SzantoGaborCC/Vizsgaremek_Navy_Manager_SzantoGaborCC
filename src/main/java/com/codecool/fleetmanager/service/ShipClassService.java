package com.codecool.fleetmanager.service;

import com.codecool.fleetmanager.DTO.CountryDTO;
import com.codecool.fleetmanager.DTO.GunDTO;
import com.codecool.fleetmanager.DTO.ShipClassDTO;
import com.codecool.fleetmanager.dao.CountryDao;
import com.codecool.fleetmanager.dao.GunDao;
import com.codecool.fleetmanager.dao.ShipClassDao;
import com.codecool.fleetmanager.dao.ShipClassesAndGunsDao;
import com.codecool.fleetmanager.model.ShipClass;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ShipClassService {
    private final ShipClassDao shipClassDao;
    private final CountryDao countryDao;

    private final GunDao gunDao;

    private final ShipClassesAndGunsDao shipClassesAndGunsDao;

    public ShipClassService(ShipClassDao shipClassDao, CountryDao countryDao,
                            GunDao gunDao, ShipClassesAndGunsDao shipClassesAndGunsDao) {
        this.shipClassDao = shipClassDao;
        this.countryDao = countryDao;
        this.gunDao = gunDao;
        this.shipClassesAndGunsDao = shipClassesAndGunsDao;
    }

    public List<ShipClassDTO> findAll() {
        return shipClassDao.findAll().stream().map(this::createShipClassDTOWithDependencies).toList();
    }

    public ShipClassDTO findById(long id) {
        ShipClass shipClass = shipClassDao.findById(id).orElseThrow();
        return createShipClassDTOWithDependencies(shipClass);
    }

    private ShipClassDTO createShipClassDTOWithDependencies(ShipClass shipClass) {
        ShipClassDTO shipClassDTO = new ShipClassDTO(shipClass);
        shipClassDTO.setCountry(new CountryDTO(countryDao.findById(shipClass.getCountryId()).orElseThrow()));
        shipClassDTO.setGuns(getShipGunsFromIds(shipClass.getId()));
        return shipClassDTO;
    }

    private Map<GunDTO, Integer> getShipGunsFromIds(long shipClassId) {
        Map<Long, Integer> gunIdsAndQuantities = shipClassesAndGunsDao.findGunIdsByShipClassId(shipClassId);
        return gunIdsAndQuantities.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> new GunDTO(gunDao.findById(entry.getKey()).orElseThrow()),
                        Map.Entry::getValue));
    }

    @Transactional
    public void add(ShipClassDTO shipClassDTO) {
        shipClassDao.add(shipClassDTO.convertToShipClass());
    }

    @Transactional
    public void update(ShipClassDTO shipClassDTO, long id) {
        shipClassDao.update(shipClassDTO.convertToShipClass(), id);
    }

    @Transactional
    public void delete(long id) {
        shipClassDao.delete(id);
    }
}
