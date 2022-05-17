package com.codecool.navymanager.dao;

import com.codecool.navymanager.model.Gun;

import java.util.List;
import java.util.Optional;

public interface GunDao {
    List<Gun> findAll();

    Optional<Gun> findById(long id);

    void add(Gun gun);

    void update(Gun gun, long id);

    void delete(long id);
}
