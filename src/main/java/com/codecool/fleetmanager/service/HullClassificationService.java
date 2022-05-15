package com.codecool.fleetmanager.service;

import com.codecool.fleetmanager.dao.HullClassificationDao;
import com.codecool.fleetmanager.model.HullClassification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
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

    @Transactional
    public void add(HullClassification hullClassification) {
        hullClassificationDao.add(hullClassification);
    }

    @Transactional
    public void update(HullClassification hullClassification, String abbreviation) {
        hullClassificationDao.update(hullClassification, abbreviation);
    }

    @Transactional
    public void delete(String abbreviation) {
        hullClassificationDao.delete(abbreviation);
    }
}
