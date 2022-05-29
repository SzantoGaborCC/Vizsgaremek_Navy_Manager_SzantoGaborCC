package com.codecool.navymanager.repository;

import com.codecool.navymanager.entity.ShipClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipClassRepository extends JpaRepository<ShipClass, Long> {
}