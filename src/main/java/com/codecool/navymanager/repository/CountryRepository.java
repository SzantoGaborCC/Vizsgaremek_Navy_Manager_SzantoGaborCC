package com.codecool.navymanager.repository;

import com.codecool.navymanager.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {
}