package com.codecool.navymanager.repository;

import com.codecool.navymanager.entity.GunAndQuantity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GunAndQuantityRepository extends JpaRepository<GunAndQuantity, Long> {
}