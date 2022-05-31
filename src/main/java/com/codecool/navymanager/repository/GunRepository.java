package com.codecool.navymanager.repository;

import com.codecool.navymanager.entity.Gun;
import com.codecool.navymanager.entityDTO.GunDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface GunRepository extends JpaRepository<Gun, Long> {
    List<Gun> findByCountryId(long countryId);
}