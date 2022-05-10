package com.codecool.fleetmanager.service;

import com.codecool.fleetmanager.dao.HullClassificationDao;
import com.codecool.fleetmanager.model.HullClassification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HullClassificationService {
    private HullClassificationDao hullClassificationDao;

    public HullClassificationService(HullClassificationDao hullClassificationDao) {
        this.hullClassificationDao = hullClassificationDao;
    }

    public List<HullClassification> findAll() {
        return hullClassificationDao.findAll();
    }

    public HullClassification findByAbbreviation(String abbreviation) {
        return hullClassificationDao.findByAbbreviation(abbreviation).orElseThrow();
    }

    public void add(HullClassification hullClassification) {
        hullClassificationDao.add(hullClassification);
    }

    public void update(HullClassification hullClassification, String abbreviation) {
        hullClassificationDao.update(hullClassification, abbreviation);
    }

    public void delete(String abbreviation) {
        hullClassificationDao.delete(abbreviation);
    }
}
