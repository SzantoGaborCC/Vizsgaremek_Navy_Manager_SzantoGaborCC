package com.codecool.navymanager.repository;

import com.codecool.navymanager.entity.Fleet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FleetRepository extends JpaRepository<Fleet, Long> {
}