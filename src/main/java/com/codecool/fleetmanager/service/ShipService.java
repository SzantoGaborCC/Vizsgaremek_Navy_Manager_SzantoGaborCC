package com.codecool.fleetmanager.service;

import com.codecool.fleetmanager.dao.GunDao;
import com.codecool.fleetmanager.dao.ShipDao;
import com.codecool.fleetmanager.dao.ShipsAndGunsDao;
import com.codecool.fleetmanager.model.Ship;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ShipService {
    private ShipDao shipDao;
    private GunDao gunDao;

    private ShipsAndGunsDao shipsAndGunsDao;

    public ShipService(ShipDao shipDao, GunDao gunDao, ShipsAndGunsDao shipsAndGunsDao) {
        this.shipDao = shipDao;
        this.gunDao = gunDao;
        this.shipsAndGunsDao = shipsAndGunsDao;
    }

    public List<Ship> findAll() {
        List<Ship> ships = shipDao.findAll();
        for (Ship ship : ships) {
            ship.setGuns(shipsAndGunsDao.findGunsByShipId(ship.getId()));
        }
        return ships;
    }

    public Ship findById(long id) {
        Ship ship = shipDao.findById(id).orElseThrow();
        ship.setGuns(shipsAndGunsDao.findGunsByShipId(ship.getId()));
        return ship;
    }

    @Transactional
    public void add(Ship ship) {
        shipDao.add(ship);
    }

    @Transactional
    public void update(Ship ship, long id) {
        shipDao.update(ship, id);
    }

    @Transactional
    public void delete(long id) {
        shipDao.delete(id);
    }
}
