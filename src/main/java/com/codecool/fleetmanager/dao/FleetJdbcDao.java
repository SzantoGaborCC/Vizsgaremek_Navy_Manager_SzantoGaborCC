package com.codecool.fleetmanager.dao;

import com.codecool.fleetmanager.model.Fleet;
import com.codecool.fleetmanager.model.mapper.FleetMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FleetJdbcDao implements FleetDao {
    private final JdbcTemplate jdbcTemplate;
    private final FleetMapper fleetMapper;

    public FleetJdbcDao(JdbcTemplate jdbcTemplate, FleetMapper fleetMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.fleetMapper = fleetMapper;
    }

    @Override
    public List<Fleet> findAll() {
        String query = "SELECT * FROM fleet";
        return jdbcTemplate.query(query, fleetMapper);
    }

    @Override
    public Optional<Fleet> findById(long id) {
        String query = "SELECT * FROM fleet WHERE id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(query, fleetMapper, id));
    }

    @Override
    public void add(Fleet fleet) {
        String query = "INSERT INTO fleet " +
                "(designation, " +
                "minimum_rank_precedence, " +
                "commander_id, " +
                "country_id) VALUES (?,?,?,?)";
       jdbcTemplate.update(query,
               fleet.getDesignation(), fleet.getMinimumRankPrecedence(),
               fleet.getCommanderId(), fleet.getCountryId());
    }

    @Override
    public void update(Fleet fleet, long id) {
        String query = "UPDATE fleet SET " +
                "designation = ?, " +
                "minimum_rank_precedence = ?, " +
                "commander_id = ?, " +
                "country_id = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(query,
                fleet.getDesignation(), fleet.getMinimumRankPrecedence(),
                fleet.getCommanderId(), fleet.getCountryId(), id);
    }

    @Override
    public void delete(long id) {
        String query = "DELETE FROM fleet WHERE id = ?";
        this.jdbcTemplate.update(query, id);
    }
}
