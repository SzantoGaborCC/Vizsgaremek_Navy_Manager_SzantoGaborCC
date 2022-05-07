package com.codecool.fleetmanager.dao;

import com.codecool.fleetmanager.model.ShipClass;

import java.util.List;
import java.util.Optional;

public interface ShipClassDao {
    List<ShipClass> findAll();

    Optional<ShipClass> findById(long id);

    void add(ShipClass shipClass);

    void update(ShipClass shipClass, long id);

    void delete(long id);
}
