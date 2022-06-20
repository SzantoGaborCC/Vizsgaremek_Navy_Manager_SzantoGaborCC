package com.codecool.navymanager.repository;

import com.codecool.navymanager.entity.Country;
import com.codecool.navymanager.entity.Officer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OfficerRepository extends JpaRepository<Officer, Long> {
    @Query("SELECT o FROM Officer o WHERE" +
            " NOT EXISTS (" +
            "   SELECT s.captain FROM Ship s WHERE s.captain = o)" +
            " AND" +
            " NOT EXISTS (" +
            "   SELECT f.commander FROM Fleet f WHERE f.commander = o)" +
            " AND" +
            " o.country = ?1")
    List<Officer> findAvailableOfficersByCountry(Country country);

    @Query("SELECT o FROM Officer o WHERE" +
            " NOT EXISTS (" +
            "   SELECT s.captain FROM Ship s WHERE s.captain = o)" +
            " AND" +
            " NOT EXISTS (" +
            "   SELECT f.commander FROM Fleet f WHERE f.commander = o)")
    List<Officer> findAvailableOfficers();
}