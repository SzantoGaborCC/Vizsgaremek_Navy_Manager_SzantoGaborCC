package com.codecool.fleetmanager.service;

import com.codecool.fleetmanager.dao.FleetDao;
import com.codecool.fleetmanager.model.Fleet;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FleetService {
    private FleetDao fleetDao;

    public FleetService(FleetDao fleetDao) {
        this.fleetDao = fleetDao;
    }

    public List<Fleet> findAll() {
        return fleetDao.findAll();
    }

    public Fleet findById(long id) {
        return fleetDao.findById(id).orElseThrow();
    }

    public void add(Fleet fleet) {
        fleetDao.add(fleet);
    }

    public void update(Fleet fleet, long id) {
        fleetDao.update(fleet, id);
    }

    public void delete(long id) {
        fleetDao.delete(id);
    }
}
