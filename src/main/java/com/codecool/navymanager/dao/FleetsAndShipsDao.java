package com.codecool.navymanager.dao;

import java.util.Map;
import java.util.Set;

public interface FleetsAndShipsDao {
    Map<Long, Set<Long>> findAll();
    Set<Long> findShipIdsByFleetId(long shipClassId);
    void addShipToFleet(long fleetId, long shipId);

    void updateShipForAFleet(long fleetId, long oldShipId, long newShipId);

    void deleteShipFromFleet(long fleetId, long shipId);
}
