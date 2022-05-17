package com.codecool.navymanager.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ShipClassesAndGunsJdbcDao implements ShipClassesAndGunsDao {
    JdbcTemplate jdbcTemplate;

    public ShipClassesAndGunsJdbcDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Map<Long, Map<Long, Integer>> findAll() {
        String query = "SELECT * FROM ship_classes_and_guns";
        Map<Long, Map<Long,Integer>> shipClassesAndGuns = new HashMap<>();
        jdbcTemplate.query(query, (rs) -> {
            Long shipClassId = rs.getLong("ship_class_id");
            Map<Long, Integer> guns = shipClassesAndGuns.computeIfAbsent(shipClassId, key -> new HashMap<>());
            Long gunId = rs.getLong("gun_id");
            Integer gunQuantity = rs.getInt("gun_quantity");
            guns.put(gunId, gunQuantity);
        });
        return shipClassesAndGuns;
    }

    @Override
    public Map<Long, Integer> findGunIdsByShipClassId(long shipClassId) {
        String query = "SELECT * FROM ship_classes_and_guns WHERE ship_class_id = ?";
        Map<Long, Integer> guns = new HashMap<>();
        jdbcTemplate.query(query, (rs) -> {
            Long gunId = rs.getLong("gun_id");
            Integer gunQuantity = rs.getInt("gun_quantity");
            guns.put(gunId, gunQuantity);
        }, shipClassId);
        return guns;
    }

    @Override
    public void addGunToShipClass(long shipClassId, long gunId, int gunQuantity) {
        String query = "INSERT INTO ship_classes_and_guns (" +
                "ship_class_id, " +
                "gun_id, " +
                "gun_quantity" +
                ") VALUES " +
                "(?,?,?)";
        jdbcTemplate.update(query, shipClassId, gunId, gunQuantity);
    }

    @Override
    public void updateGunForAShipClass(long shipClassId, long oldGunId, long newGunId, int gunQuantity) {
        String query = "UPDATE ship_classes_and_guns SET " +
                "gun_id = ?, " +
                "gun_quantity = ? " +
                "WHERE ship_class_id = ? AND gun_id = ?";
        jdbcTemplate.update(query, newGunId, gunQuantity, shipClassId, oldGunId);
    }

    @Override
    public void deleteGunFromShipClass(long shipClassId, long gunId) {
        String query = "DELETE FROM ship_classes_and_guns WHERE ship_class_id = ? AND gun_id = ?";
        jdbcTemplate.update(query, shipClassId, gunId);
    }
}
