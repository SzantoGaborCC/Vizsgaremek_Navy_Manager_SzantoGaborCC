package com.codecool.fleetmanager.dao;

import java.util.Map;

public interface ShipClassesAndGunsDao {
    Map<Long, Map<Long, Integer>> findAll();
    Map<Long, Integer> findGunIdsByShipClassId(long shipClassId);
    void addGunToShipClass(long shipClassId, long gunId, int gunQuantity);

    void updateGunForAShipClass(long shipClassId, long oldGunId, long newGunId, int gunQuantity);

    void deleteGunFromShipClass(long shipClassId, long gunId);
}
