package com.codecool.navymanager.service;

import com.codecool.navymanager.DTO.GunDTO;
import com.codecool.navymanager.DTO.ShipClassDTO;
import com.codecool.navymanager.dao.ShipClassDao;
import com.codecool.navymanager.dao.ShipClassesAndGunsDao;
import com.codecool.navymanager.model.ShipClass;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ShipClassService {
    private final ShipClassDao shipClassDao;
    private final CountryService countryService;

    private final HullClassificationService hullClassificationService;
    private final GunService gunService;
    private final ShipClassesAndGunsDao shipClassesAndGunsDao;

    public ShipClassService(ShipClassDao shipClassDao, CountryService countryService, HullClassificationService hullClassificationService, GunService gunService, ShipClassesAndGunsDao shipClassesAndGunsDao) {
        this.shipClassDao = shipClassDao;
        this.countryService = countryService;
        this.hullClassificationService = hullClassificationService;
        this.gunService = gunService;
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
        shipClassDTO.setHullClassification(hullClassificationService.findByAbbreviation(shipClass.getHullClassification()));
        shipClassDTO.setCountry(countryService.findById(shipClass.getCountryId()));
        shipClassDTO.setGuns(getShipGunsFromIds(shipClass.getId()));
        return shipClassDTO;
    }

    private Map<GunDTO, Integer> getShipGunsFromIds(long shipClassId) {
        Map<Long, Integer> gunIdsAndQuantities = shipClassesAndGunsDao.findGunIdsByShipClassId(shipClassId);
        return gunIdsAndQuantities.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> gunService.findById(entry.getKey()),
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
