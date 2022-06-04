package com.codecool.navymanager.repository;

import com.codecool.navymanager.entity.Country;
import com.codecool.navymanager.entity.Ship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Arrays;
import java.util.List;

public interface ShipRepository extends JpaRepository<Ship, Long> {
    List<Ship> findByCountry(Country country);
    @Query("SELECT s FROM Ship s WHERE s.fleet IS NULL AND s.country = ?1")
    List<Ship> findAvailableShipsByCountry(Country country);
}