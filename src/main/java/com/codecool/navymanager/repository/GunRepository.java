package com.codecool.navymanager.repository;

import com.codecool.navymanager.entity.Gun;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GunRepository extends JpaRepository<Gun, Long> {
}