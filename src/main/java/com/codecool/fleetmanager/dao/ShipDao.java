package com.codecool.fleetmanager.dao;

import com.codecool.fleetmanager.model.Ship;

import java.util.List;
import java.util.Optional;

public interface ShipDao {
    List<Ship> findAll();

    Optional<Ship> findById(long id);

    void add(Ship ship);

    void update(Ship ship, long id);

    void delete(long id);
}
