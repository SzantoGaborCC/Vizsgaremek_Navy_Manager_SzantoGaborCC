package com.codecool.navymanager.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Repository
public class FleetsAndShipsJdbcDao implements FleetsAndShipsDao {
    JdbcTemplate jdbcTemplate;

    public FleetsAndShipsJdbcDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Map<Long, Set<Long>> findAll() {
        String query = "SELECT * FROM fleets_and_ships";
        Map<Long, Set<Long>> fleetsAndShips = new HashMap<>();
        jdbcTemplate.query(query, (rs) -> {
            Long fleetId = rs.getLong("fleet_id");
            Set<Long> ships = fleetsAndShips.computeIfAbsent(fleetId, key -> new HashSet<>());
            Long shipId = rs.getLong("ship_id");
            ships.add(shipId);
        });
        return fleetsAndShips;
    }

    @Override
    public Set<Long> findShipIdsByFleetId(long fleetId) {
        String query = "SELECT * FROM fleets_and_ships WHERE fleet_id = ?";
        Set<Long> ships = new HashSet<>();
        jdbcTemplate.query(query, (rs) -> {
            Long shipId = rs.getLong("ship_id");
            ships.add(shipId);
        }, fleetId);
        return ships;
    }

    @Override
    public void addShipToFleet(long fleetId, long shipId) {
        String query = "INSERT INTO fleets_and_ships (" +
                "fleet_id, " +
                "ship_id " +
                ") VALUES " +
                "(?,?)";
        jdbcTemplate.update(query, fleetId, shipId);
    }

    @Override
    public void updateShipForAFleet(long fleetId, long oldShipId, long newShipId) {
        String query = "UPDATE fleets_and_ships SET " +
                "ship_id = ? " +
                "WHERE fleet_id = ? AND ship_id = ?";
        jdbcTemplate.update(query, newShipId, fleetId, oldShipId);
    }

    @Override
    public void deleteShipFromFleet(long fleetId, long shipId) {
        String query = "DELETE FROM fleets_and_ships WHERE fleet_id = ? AND ship_id = ?";
        jdbcTemplate.update(query, fleetId, shipId);
    }
}
