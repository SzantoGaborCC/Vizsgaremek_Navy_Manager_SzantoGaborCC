package com.codecool.navymanager.repository;

import com.codecool.navymanager.entity.GunInstallation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GunAndQuantityRepository extends JpaRepository<GunInstallation, Long> {
}