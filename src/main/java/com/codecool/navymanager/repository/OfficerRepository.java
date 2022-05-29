package com.codecool.navymanager.repository;

import com.codecool.navymanager.entity.Officer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfficerRepository extends JpaRepository<Officer, Long> {
}