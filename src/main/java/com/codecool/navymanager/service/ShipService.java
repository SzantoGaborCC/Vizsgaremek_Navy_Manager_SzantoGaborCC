package com.codecool.navymanager.service;

import com.codecool.navymanager.DTO.ShipDTO;
import com.codecool.navymanager.dao.ShipDao;
import com.codecool.navymanager.model.Ship;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ShipService {
    private final ShipDao shipDao;

    private final ShipClassService shipClassService;
    private final OfficerService officerService;

    private final CountryService countryService;

    public ShipService(ShipDao shipDao, ShipClassService shipClassService, OfficerService officerService, CountryService countryService) {
        this.shipDao = shipDao;
        this.shipClassService = shipClassService;
        this.officerService = officerService;
        this.countryService = countryService;
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
        shipDTO.setShipClass(shipClassService.findById(ship.getShipClassId()));
        shipDTO.setCaptain(officerService.findById(ship.getCaptainId()));
        shipDTO.setCountry(countryService.findById(ship.getCountryId()));
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
