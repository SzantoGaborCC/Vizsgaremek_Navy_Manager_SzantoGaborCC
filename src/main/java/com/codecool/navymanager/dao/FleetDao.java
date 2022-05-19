package com.codecool.navymanager.dao;

import com.codecool.navymanager.model.Fleet;

import java.util.List;
import java.util.Optional;

public interface FleetDao {
    List<Fleet> findAll();

    Optional<Fleet> findById(long id);

    void add(Fleet fleet);

    void update(Fleet fleet, long id);

    void delete(long id);
}