package com.codecool.navymanager.dao;

import com.codecool.navymanager.model.Ship;

import java.util.List;
import java.util.Optional;

public interface ShipDao {
    List<Ship> findAll();

    Optional<Ship> findById(long id);

    void add(Ship ship);

    void update(Ship ship, long id);

    void delete(long id);

    List<Ship> findByCountryId(long countryId);

    List<Ship> findByShipClassId(long shipClassId);

    List<Ship> findByShipClassIdAndCountryId(long shipClassId, long countryId);
}
