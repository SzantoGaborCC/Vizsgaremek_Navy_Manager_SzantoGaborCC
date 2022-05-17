package com.codecool.navymanager.dao;

import com.codecool.navymanager.model.ShipClass;
import com.codecool.navymanager.model.mapper.ShipClassMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ShipClassJdbcDao implements ShipClassDao {
    JdbcTemplate jdbcTemplate;
    ShipClassMapper shipClassMapper;

    public ShipClassJdbcDao(JdbcTemplate jdbcTemplate, ShipClassMapper shipClassMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.shipClassMapper = shipClassMapper;
    }

    @Override
    public List<ShipClass> findAll() {
        String query = "SELECT * FROM ship_class";
        return jdbcTemplate.query(query, shipClassMapper);
    }

    @Override
    public Optional<ShipClass> findById(long id) {
        String query = "SELECT * FROM ship_class WHERE id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(query, shipClassMapper, id));
    }

    @Override
    public void add(ShipClass shipClass) {
        String query = "INSERT INTO ship_class " +
                "(name, " +
                "displacement_in_tons, " +
                "hull_classification, " +
                "armor_belt_in_cms " +
                "armor_turret_in_cms " +
                "armor_deck_in_cms " +
                "speed_in_kmh " +
                ") VALUES (?,?,?,?,?,?,?)";
       jdbcTemplate.update(query,
               shipClass.getName(), shipClass.getDisplacementInTons(), shipClass.getHullClassification(),
               shipClass.getArmorBeltInCms(), shipClass.getArmorTurretInCms(), shipClass.getArmorDeckInCms(),
               shipClass.getSpeedInKmh());
    }

    @Override
    public void update(ShipClass shipClass, long id) {
        String query = "UPDATE ship_class SET " +
                "name = ?, " +
                "displacement_in_tons = ?, " +
                "hull_classification = ?, " +
                "armor_belt_in_cms = ?, " +
                "armor_turret_in_cms = ?, " +
                "armor_deck_in_cms = ?, " +
                "speed_in_kmh = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(query,
                shipClass.getName(), shipClass.getDisplacementInTons(), shipClass.getHullClassification(),
                shipClass.getArmorBeltInCms(), shipClass.getArmorTurretInCms(), shipClass.getArmorDeckInCms(),
                shipClass.getSpeedInKmh(), id);
    }

    @Override
    public void delete(long id) {
        String query = "DELETE FROM ship_class WHERE id = ?";
        this.jdbcTemplate.update(query, id);
    }
}
