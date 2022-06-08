package com.codecool.navymanager.repository;

import com.codecool.navymanager.entity.HullClassification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HullClassificationRepository extends JpaRepository<HullClassification, Long> {
    HullClassification findById(long id);
    boolean existsById(long id);
    void deleteById(long id);
}