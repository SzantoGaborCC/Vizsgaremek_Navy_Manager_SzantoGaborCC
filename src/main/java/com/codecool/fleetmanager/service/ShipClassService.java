package com.codecool.fleetmanager.service;

import com.codecool.fleetmanager.dao.ShipClassDao;
import com.codecool.fleetmanager.model.ShipClass;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ShipClassService {
    private ShipClassDao shipClassDao;

    public ShipClassService(ShipClassDao shipClassDao) {
        this.shipClassDao = shipClassDao;
    }

    public List<ShipClass> findAll() {
        return shipClassDao.findAll();
    }

    public ShipClass findById(long id) {
        return shipClassDao.findById(id).orElseThrow();
    }

    @Transactional
    public void add(ShipClass shipClass) {
        shipClassDao.add(shipClass);
    }

    @Transactional
    public void update(ShipClass shipClass, long id) {
        shipClassDao.update(shipClass, id);
    }

    @Transactional
    public void delete(long id) {
        shipClassDao.delete(id);
    }
}
