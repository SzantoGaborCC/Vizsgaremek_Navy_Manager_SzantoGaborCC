package com.codecool.navymanager.repository;

import com.codecool.navymanager.entity.ShipClassesAndGuns;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipClassesAndGunsRepository extends JpaRepository<ShipClassesAndGuns, Long> {
}