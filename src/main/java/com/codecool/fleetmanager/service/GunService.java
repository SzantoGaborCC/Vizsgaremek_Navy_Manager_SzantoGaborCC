package com.codecool.fleetmanager.service;

import com.codecool.fleetmanager.dao.GunDao;
import com.codecool.fleetmanager.model.Gun;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class GunService {
    private GunDao gunDao;

    public GunService(GunDao gunDao) {
        this.gunDao = gunDao;
    }

    public List<Gun> findAll() {
        return gunDao.findAll();
    }

    public Gun findById(long id) {
        return gunDao.findById(id).orElseThrow();
    }

    @Transactional
    public void add(Gun gun) {
        gunDao.add(gun);
    }

    @Transactional
    public void update(Gun gun, long id) {
        gunDao.update(gun, id);
    }

    @Transactional
    public void delete(long id) {
        gunDao.delete(id);
    }
}
