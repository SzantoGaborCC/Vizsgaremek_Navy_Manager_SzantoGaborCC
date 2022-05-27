package com.codecool.navymanager.service;

import com.codecool.navymanager.DTO.FleetDTO;
import com.codecool.navymanager.DTO.ShipDTO;
import com.codecool.navymanager.dao.FleetDao;
import com.codecool.navymanager.dao.FleetsAndShipsDao;
import com.codecool.navymanager.model.Fleet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FleetService {
    private final FleetDao fleetDao;

    private  final RankService rankService;
    private final OfficerService officerService;
    private final CountryService countryService;
    private final ShipService shipService;

    private final FleetsAndShipsDao fleetsAndShipsDao;

    public FleetService(FleetDao fleetDao, RankService rankService,
                        OfficerService officerService, CountryService countryService,
                        ShipService shipService, FleetsAndShipsDao fleetsAndShipsDao) {
        this.fleetDao = fleetDao;
        this.rankService = rankService;
        this.officerService = officerService;
        this.countryService = countryService;
        this.shipService = shipService;
        this.fleetsAndShipsDao = fleetsAndShipsDao;
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
        fleetDTO.setMinimumRank(rankService.findByPrecedence(fleet.getMinimumRankPrecedence()));
        fleetDTO.setCommander(officerService.findById(fleet.getCommanderId()));
        fleetDTO.setCountry(countryService.findById(fleet.getCountryId()));
        fleetDTO.setShips(getShipsFromIds(fleetDTO.getId()));
        return fleetDTO;
    }

    private Set<ShipDTO> getShipsFromIds(long fleetId) {
        Set<Long> shipIds = fleetsAndShipsDao.findShipIdsByFleetId(fleetId);
        return shipIds.stream().map(shipService::findById).collect(Collectors.toSet());
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
    @Transactional
    public void addShipToFleet(long fleetId, long shipId) {
        fleetsAndShipsDao.addShipToFleet(fleetId, shipId);
    }
    @Transactional
    public void updateShipForAFleet(long fleetId, long oldShipId, long newShipId) {
        fleetsAndShipsDao.updateShipForAFleet(fleetId, oldShipId, newShipId);
    }
    @Transactional
    public void deleteShipFromFleet(long fleetId, long shipId) {
        fleetsAndShipsDao.deleteShipFromFleet(fleetId, shipId);
    }
}
