package com.codecool.navymanager.repository;

import com.codecool.navymanager.entity.Fleet;
import com.codecool.navymanager.entity.Ship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface FleetRepository extends JpaRepository<Fleet, Long> {
    Set<Ship> findShips(long fleetId);
}