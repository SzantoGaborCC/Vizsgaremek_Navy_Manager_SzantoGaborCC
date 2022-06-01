package com.codecool.navymanager.repository;

import com.codecool.navymanager.entity.Country;
import com.codecool.navymanager.entity.Ship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface ShipRepository extends JpaRepository<Ship, Long> {
    List<Ship> findByCountry(Country country);
}