package com.codecool.fleetmanager.dao;

import com.codecool.fleetmanager.model.Officer;

import java.util.List;
import java.util.Optional;

public interface OfficerDao {
    List<Officer> findAll();

    Optional<Officer> findById(long id);

    void add(Officer officer);

    void update(Officer officer, long id);

    void delete(long id);
}
