package com.codecool.fleetmanager.service;

import com.codecool.fleetmanager.DTO.CountryDTO;
import com.codecool.fleetmanager.DTO.OfficerDTO;
import com.codecool.fleetmanager.DTO.ShipClassDTO;
import com.codecool.fleetmanager.DTO.ShipDTO;
import com.codecool.fleetmanager.dao.CountryDao;
import com.codecool.fleetmanager.dao.OfficerDao;
import com.codecool.fleetmanager.dao.ShipClassDao;
import com.codecool.fleetmanager.dao.ShipDao;
import com.codecool.fleetmanager.model.Ship;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ShipService {
    private final ShipDao shipDao;

    private final ShipClassDao shipClassDao;
    private final OfficerDao officerDao;

    private final CountryDao countryDao;

    public ShipService(ShipDao shipDao, ShipClassDao shipClassDao, OfficerDao officerDao, CountryDao countryDao) {
        this.shipDao = shipDao;
        this.shipClassDao = shipClassDao;
        this.officerDao = officerDao;
        this.countryDao = countryDao;
    }

    public List<ShipDTO> findAll() {
        return shipDao.findAll().stream().map(this::createShipDTOWithDependencies).toList();
    }

    public ShipDTO findById(long id) {
        Ship ship = shipDao.findById(id).orElseThrow();
        return createShipDTOWithDependencies(ship);
    }

    private ShipDTO createShipDTOWithDependencies(Ship ship) {
        ShipDTO shipDTO = new ShipDTO(ship);
        shipDTO.setShipClass(new ShipClassDTO(shipClassDao.findById(ship.getShipClassId()).orElseThrow()));
        shipDTO.setCaptain(new OfficerDTO(officerDao.findById(ship.getCaptainId()).orElseThrow()));
        shipDTO.setCountry(new CountryDTO(countryDao.findById(ship.getCountryId()).orElseThrow()));
        return shipDTO;
    }

    @Transactional
    public void add(ShipDTO shipDTO) {
        shipDao.add(shipDTO.convertToShip());
    }

    @Transactional
    public void update(ShipDTO shipDTO, long id) {
        shipDao.update(shipDTO.convertToShip(), id);
    }

    @Transactional
    public void delete(long id) {
        shipDao.delete(id);
    }
}
