package com.codecool.navymanager.repository;

import com.codecool.navymanager.entity.HullClassification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HullClassificationRepository extends JpaRepository<HullClassification, String> {
    HullClassification findByAbbreviation(String abbreviation);
    boolean existsByAbbreviation(String abbreviation);
    void deleteByAbbreviation(String abbreviation);
}