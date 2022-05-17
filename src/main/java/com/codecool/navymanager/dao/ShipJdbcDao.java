package com.codecool.navymanager.dao;

import com.codecool.navymanager.model.Ship;
import com.codecool.navymanager.model.mapper.ShipMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ShipJdbcDao implements ShipDao {
    JdbcTemplate jdbcTemplate;
    ShipMapper shipMapper;

    public ShipJdbcDao(JdbcTemplate jdbcTemplate, ShipMapper shipMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.shipMapper = shipMapper;
    }

    @Override
    public List<Ship> findAll() {
        String query = "SELECT * FROM ship";
        return jdbcTemplate.query(query, shipMapper);
    }

    @Override
    public Optional<Ship> findById(long id) {
        String query = "SELECT * FROM ship WHERE id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(query, shipMapper, id));
    }

    @Override
    public void add(Ship ship) {
        String query = "INSERT INTO ship " +
                "(name, " +
                "ship_class_id, " +
                "captain_id, " +
                "country_id " +
                ") VALUES (?,?,?,?)";
       jdbcTemplate.update(query,
               ship.getName(), ship.getShipClassId(), ship.getShipClassId(),ship.getCaptainId());
    }

    @Override
    public void update(Ship ship, long id) {
        String query = "UPDATE ship SET " +
                "name = ?, " +
                "ship_class_id = ?, " +
                "captain_id = ?, " +
                "country_id = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(query,
                ship.getName(), ship.getShipClassId(), ship.getShipClassId(),ship.getCaptainId(), id);
    }

    @Override
    public void delete(long id) {
        String query = "DELETE FROM ship WHERE id = ?";
        this.jdbcTemplate.update(query, id);
    }

    @Override
    public List<Ship> findByCountryId(long countryId) {
        String query = "SELECT * FROM ship WHERE country_id = ?";
        return jdbcTemplate.query(query, shipMapper, countryId);
    }

    @Override
    public List<Ship> findByShipClassId(long shipClassId) {
        String query = "SELECT * FROM ship WHERE ship_class_id = ?";
        return jdbcTemplate.query(query, shipMapper, shipClassId);
    }

    @Override
    public List<Ship> findByShipClassIdAndCountryId(long shipClassId, long countryId) {
        String query = "SELECT * FROM ship WHERE ship_class_id = ? AND country_id = ?";
        return jdbcTemplate.query(query, shipMapper, shipClassId, countryId);
    }
}
