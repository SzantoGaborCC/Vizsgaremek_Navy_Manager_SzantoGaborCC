package com.codecool.navymanager.repository;

import com.codecool.navymanager.entity.Fleet;
import com.codecool.navymanager.entity.Officer;
import com.codecool.navymanager.entity.Ship;
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
            " o.country.id = ?1")
    List<Officer> findAvailableOfficersByCountry(long countryId);

    @Query("SELECT o FROM Officer o WHERE" +
            " NOT EXISTS (" +
            "   SELECT s.captain FROM Ship s WHERE s.captain = o)" +
            " AND" +
            " NOT EXISTS (" +
            "   SELECT f.commander FROM Fleet f WHERE f.commander = o)")
    List<Officer> findAvailableOfficers();

    @Query("SELECT f FROM Fleet f JOIN FETCH Officer o ON f.commander = o WHERE f.commander = ?1")
    Fleet findFleetPost(Officer officer);

    @Query("SELECT s FROM Ship s JOIN FETCH Officer o ON s.captain = o WHERE s.captain = ?1")
    Ship findShipPost(Officer officer);
}