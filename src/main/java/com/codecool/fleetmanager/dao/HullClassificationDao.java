package com.codecool.fleetmanager.dao;

import com.codecool.fleetmanager.model.HullClassification;

import java.util.List;
import java.util.Optional;

public interface HullClassificationDao {
    List<HullClassification> findAll();

    Optional<HullClassification> findByAbbreviation(String abbreviation);

    void add(HullClassification hullClassification);

    void update(HullClassification hullClassification, String abbreviation);

    void delete(String abbreviation);
}
