package com.codecool.fleetmanager.dao;

import java.util.Map;

public interface ShipsAndGunsDao {
    Map<Long, Map<Long, Integer>> findAll();
    Map<Long, Integer> findGunsByShipId(long shipId);
    void addGunToShip(long shipId, long gunId, int gunQuantity);

    void updateGunForAShip(long shipId, long oldGunId, long newGunId, int gunQuantity);

    void deleteGunFromShip(long shipId, long gunId);
}
