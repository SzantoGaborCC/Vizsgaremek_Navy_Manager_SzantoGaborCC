package com.codecool.navymanager.service;

import com.codecool.navymanager.DTO.*;
import com.codecool.navymanager.dao.OfficerDao;
import com.codecool.navymanager.model.Officer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OfficerService {
    private final OfficerDao officerDao;
    private final RankService rankService;

   private final CountryService countryService;

    public OfficerService(OfficerDao officerDao, RankService rankService, CountryService countryService) {
        this.officerDao = officerDao;
        this.rankService = rankService;
        this.countryService = countryService;
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
        officerDTO.setRank(rankService.findById(officer.getRankId()));
        officerDTO.setCountry(countryService.findById(officer.getCountryId()));
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

    public List<RankDTO> getValidRankValues() {
        return rankService.findAll();
    }

    public List<CountryDTO> getValidCountryValues() {
        return countryService.findAll();
    }
}
