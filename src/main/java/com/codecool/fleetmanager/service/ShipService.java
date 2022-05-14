package com.codecool.fleetmanager.service;

import com.codecool.fleetmanager.dao.ShipDao;
import com.codecool.fleetmanager.model.Ship;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipService {
    private ShipDao shipDao;

    public ShipService(ShipDao shipDao) {
        this.shipDao = shipDao;
    }

    public List<Ship> findAll() {
        return shipDao.findAll();
    }

    public Ship findById(long id) {
        return shipDao.findById(id).orElseThrow();
    }

    public void add(Ship ship) {
        shipDao.add(ship);
    }

    public void update(Ship ship, long id) {
        shipDao.update(ship, id);
    }

    public void delete(long id) {
        shipDao.delete(id);
    }
}
