package com.codecool.fleetmanager.service;

import com.codecool.fleetmanager.DTO.HullClassificationDTO;
import com.codecool.fleetmanager.dao.HullClassificationDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class HullClassificationService {
    private final HullClassificationDao hullClassificationDao;

    public HullClassificationService(HullClassificationDao hullClassificationDao) {
        this.hullClassificationDao = hullClassificationDao;
    }

    public List<HullClassificationDTO> findAll() {
        return hullClassificationDao.findAll().stream().map(HullClassificationDTO::new).toList();
    }

    public HullClassificationDTO findByAbbreviation(String abbreviation) {
        return new HullClassificationDTO(hullClassificationDao.findByAbbreviation(abbreviation).orElseThrow());
    }

    @Transactional
    public void add(HullClassificationDTO hullClassificationDTO) {
        hullClassificationDao.add(hullClassificationDTO.convertToHullClassification());
    }

    @Transactional
    public void update(HullClassificationDTO hullClassificationDTO, String abbreviation) {
        hullClassificationDao.update(hullClassificationDTO.convertToHullClassification(), abbreviation);
    }

    @Transactional
    public void delete(String abbreviation) {
        hullClassificationDao.delete(abbreviation);
    }
}
