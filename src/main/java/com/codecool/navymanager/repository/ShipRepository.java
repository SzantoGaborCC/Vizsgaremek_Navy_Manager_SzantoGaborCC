package com.codecool.navymanager.repository;

import com.codecool.navymanager.entity.Ship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipRepository extends JpaRepository<Ship, Long> {
}