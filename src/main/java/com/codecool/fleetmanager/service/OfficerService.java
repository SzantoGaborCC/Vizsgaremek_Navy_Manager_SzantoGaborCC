package com.codecool.fleetmanager.service;

import com.codecool.fleetmanager.DTO.*;
import com.codecool.fleetmanager.dao.CountryDao;
import com.codecool.fleetmanager.dao.OfficerDao;
import com.codecool.fleetmanager.dao.RankDao;
import com.codecool.fleetmanager.model.Officer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OfficerService {
    private OfficerDao officerDao;
    private RankDao rankDao;

    private CountryDao countryDao;

    public OfficerService(OfficerDao officerDao, RankDao rankDao, CountryDao countryDao) {
        this.officerDao = officerDao;
        this.rankDao = rankDao;
        this.countryDao = countryDao;
    }

    public List<OfficerDTO> findAll() {
        return officerDao.findAll().stream().map(this::createOfficerDTOWithDependencies).toList();
    }

    public OfficerDTO findById(long id) {
        Officer officer = officerDao.findById(id).orElseThrow();
        return createOfficerDTOWithDependencies(officer);
    }

    private OfficerDTO createOfficerDTOWithDependencies(Officer officer) {
        OfficerDTO officerDTO = new OfficerDTO(officer);
        officerDTO.setRankDTO(new RankDTO(rankDao.findById(officer.getRankId()).orElseThrow()));
        officerDTO.setCountryDTO(new CountryDTO(countryDao.findById(officer.getCountryId()).orElseThrow()));
        return officerDTO;
    }

    @Transactional
    public void add(OfficerDTO officerDTO) {
        officerDao.add(officerDTO.convertToOfficer());
    }

    @Transactional
    public void update(OfficerDTO officerDTO, long id) {
        officerDao.update(officerDTO.convertToOfficer(), id);
    }

    @Transactional
    public void delete(long id) {
        officerDao.delete(id);
    }
}
