package com.codecool.fleetmanager.service;

import com.codecool.fleetmanager.DTO.CountryDTO;
import com.codecool.fleetmanager.DTO.FleetDTO;
import com.codecool.fleetmanager.DTO.OfficerDTO;
import com.codecool.fleetmanager.dao.CountryDao;
import com.codecool.fleetmanager.dao.FleetDao;
import com.codecool.fleetmanager.dao.OfficerDao;
import com.codecool.fleetmanager.model.Fleet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class FleetService {
    private final FleetDao fleetDao;

    private final OfficerDao officerDao;

    private final CountryDao countryDao;

    public FleetService(FleetDao fleetDao, OfficerDao officerDao, CountryDao countryDao) {
        this.fleetDao = fleetDao;
        this.officerDao = officerDao;
        this.countryDao = countryDao;
    }

    public List<FleetDTO> findAll() {
        return fleetDao.findAll().stream().map(this::createFleetDTOWithDependencies).toList();
    }

    public FleetDTO findById(long id) {
        Fleet fleet = fleetDao.findById(id).orElseThrow();
        return createFleetDTOWithDependencies(fleet);
    }

    private FleetDTO createFleetDTOWithDependencies(Fleet fleet) {
        FleetDTO fleetDTO = new FleetDTO(fleet);
        fleetDTO.setCommander(new OfficerDTO(officerDao.findById(fleet.getCommanderId()).orElseThrow()));
        fleetDTO.setCountry(new CountryDTO(countryDao.findById(fleet.getCountryId()).orElseThrow()));
        return fleetDTO;
    }

    @Transactional
    public void add(FleetDTO fleetDTO) {
        fleetDao.add(fleetDTO.convertToFleet());
    }

    @Transactional
    public void update(FleetDTO fleetDTO, long id) {
        fleetDao.update(fleetDTO.convertToFleet(), id);
    }

    @Transactional
    public void delete(long id) {
        fleetDao.delete(id);
    }
}
