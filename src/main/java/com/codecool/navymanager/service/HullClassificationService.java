package com.codecool.navymanager.service;

import com.codecool.navymanager.DTO.HullClassificationDTO;
import com.codecool.navymanager.DTO.OfficerDTO;
import com.codecool.navymanager.dao.HullClassificationDao;
import com.codecool.navymanager.model.HullClassification;
import com.codecool.navymanager.model.Officer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class HullClassificationService {
    private final HullClassificationDao hullClassificationDao;
    private final RankService rankService;

    public HullClassificationService(HullClassificationDao hullClassificationDao, RankService rankService) {
        this.hullClassificationDao = hullClassificationDao;
        this.rankService = rankService;
    }

    public List<HullClassificationDTO> findAll() {
        return hullClassificationDao.findAll().stream().map(this::createHullClassificationDTOWithDependencies).toList();
    }

    public HullClassificationDTO findByAbbreviation(String abbreviation) {
        HullClassification hullClassification = hullClassificationDao.findByAbbreviation(abbreviation).orElseThrow();
        return createHullClassificationDTOWithDependencies(hullClassification);
    }

    private HullClassificationDTO createHullClassificationDTOWithDependencies(HullClassification hullClassification) {
        HullClassificationDTO hullClassificationDTO = new HullClassificationDTO(hullClassification);
        hullClassificationDTO.setMinimumRank(rankService.findByPrecedence(hullClassification.getMinimumRankPrecedence()));
        return hullClassificationDTO;
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
