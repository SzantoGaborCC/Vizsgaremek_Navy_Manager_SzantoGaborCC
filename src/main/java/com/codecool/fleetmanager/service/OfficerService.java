package com.codecool.fleetmanager.service;

import com.codecool.fleetmanager.dao.OfficerDao;
import com.codecool.fleetmanager.model.Officer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OfficerService {
    private OfficerDao officerDao;

    public OfficerService(OfficerDao officerDao) {
        this.officerDao = officerDao;
    }

    public List<Officer> findAll() {
        return officerDao.findAll();
    }

    public Officer findById(long id) {
        return officerDao.findById(id).orElseThrow();
    }

    @Transactional
    public void add(Officer officer) {
        officerDao.add(officer);
    }

    @Transactional
    public void update(Officer officer, long id) {
        officerDao.update(officer, id);
    }

    @Transactional
    public void delete(long id) {
        officerDao.delete(id);
    }
}
