package com.codecool.navymanager.repository;

import com.codecool.navymanager.entity.GunInstallation;
import com.codecool.navymanager.entity.ShipClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GunInstallationRepository extends JpaRepository<GunInstallation, Long> {
    void deleteByShipClass(ShipClass shipClass);
}