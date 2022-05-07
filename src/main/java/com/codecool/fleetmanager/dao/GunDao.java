package com.codecool.fleetmanager.dao;

import com.codecool.fleetmanager.model.Gun;

import java.util.List;
import java.util.Optional;

public interface GunDao {
    List<Gun> findAll();

    Optional<Gun> findById(long id);

    void add(Gun gun);

    void update(Gun gun, long id);

    void delete(long id);
}
