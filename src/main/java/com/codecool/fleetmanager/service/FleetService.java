package com.codecool.fleetmanager.service;

import com.codecool.fleetmanager.dao.FleetDao;
import com.codecool.fleetmanager.model.Fleet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
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

    @Transactional
    public void add(Fleet fleet) {
        fleetDao.add(fleet);
    }

    @Transactional
    public void update(Fleet fleet, long id) {
        fleetDao.update(fleet, id);
    }

    @Transactional
    public void delete(long id) {
        fleetDao.delete(id);
    }
}
