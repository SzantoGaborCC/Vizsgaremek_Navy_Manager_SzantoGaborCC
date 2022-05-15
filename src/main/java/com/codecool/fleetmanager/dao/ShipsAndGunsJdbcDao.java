package com.codecool.fleetmanager.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ShipsAndGunsJdbcDao implements ShipsAndGunsDao {
    JdbcTemplate jdbcTemplate;

    public ShipsAndGunsJdbcDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Map<Long, Map<Long, Integer>> findAll() {
        String query = "SELECT * FROM ships_and_guns";
        Map<Long, Map<Long,Integer>> shipsAndGuns = new HashMap<>();
        jdbcTemplate.query(query, (rs) -> {
            Long shipId = rs.getLong("ship_id");
            Map<Long, Integer> guns = shipsAndGuns.computeIfAbsent(shipId, key -> new HashMap<>());
            Long gunId = rs.getLong("gun_id");
            Integer gunQuantity = rs.getInt("gun_quantity");
            guns.put(gunId, gunQuantity);
        });
        return shipsAndGuns;
    }

    @Override
    public Map<Long, Integer> findGunsByShipId(long shipId) {
        String query = "SELECT * FROM ships_and_guns WHERE ship_id = ?";
        Map<Long, Integer> guns = new HashMap<>();
        jdbcTemplate.query(query, (rs) -> {
            Long gunId = rs.getLong("gun_id");
            Integer gunQuantity = rs.getInt("gun_quantity");
            guns.put(gunId, gunQuantity);
        }, shipId);
        return guns;
    }

    @Override
    public void addGunToShip(long shipId, long gunId, int gunQuantity) {
        String query = "INSERT INTO ships_and_guns (" +
                "ship_id, " +
                "gun_id, " +
                "gun_quantity" +
                ") VALUES " +
                "(?,?,?)";
        jdbcTemplate.update(query, shipId, gunId, gunQuantity);
    }

    @Override
    public void updateGunForAShip(long shipId, long oldGunId, long newGunId, int gunQuantity) {
        String query = "UPDATE ships_and_guns SET " +
                "gun_id = ?, " +
                "gun_quantity = ? " +
                "WHERE ship_id = ? AND gun_id = ?";
        jdbcTemplate.update(query, newGunId, gunQuantity, shipId, oldGunId);
    }

    @Override
    public void deleteGunFromShip(long shipId, long gunId) {
        String query = "DELETE FROM ships_and_guns WHERE ship_id = ? AND gun_id = ?";
        jdbcTemplate.update(query, shipId, gunId);
    }
}
