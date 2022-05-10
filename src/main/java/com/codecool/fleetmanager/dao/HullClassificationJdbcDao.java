package com.codecool.fleetmanager.dao;

import com.codecool.fleetmanager.model.HullClassification;
import com.codecool.fleetmanager.model.mapper.HullClassificationMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class HullClassificationJdbcDao implements HullClassificationDao {
    private JdbcTemplate jdbcTemplate;
    private HullClassificationMapper hullClassificationMapper;

    public HullClassificationJdbcDao(JdbcTemplate jdbcTemplate, HullClassificationMapper hullClassificationMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.hullClassificationMapper = hullClassificationMapper;
    }

    @Override
    public List<HullClassification> findAll() {
        String query = "SELECT * FROM hull_classification";
        return jdbcTemplate.query(query, hullClassificationMapper);
    }

    @Override
    public Optional<HullClassification> findByAbbreviation(String abbreviation) {
        String query = "SELECT * FROM hull_classification WHERE abbreviation = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(query, hullClassificationMapper, abbreviation));
    }

    @Override
    public void add(HullClassification hullClassification) {
        String query = "INSERT INTO hull_classification " +
                "(abbreviation, " +
                "designation, " +
                "minimum_rank_precedence) " +
                "VALUES (?,?,?)";
       jdbcTemplate.update(query,
               hullClassification.getAbbreviation(), hullClassification.getDesignation(),
               hullClassification.getMinimumRankPrecedence());
    }

    @Override
    public void update(HullClassification hullClassification, String abbreviation) {
        String query = "UPDATE hull_classification SET " +
                "designation = ?, " +
                "minimum_rank_precedence = ? " +
                "WHERE abbreviation = ?";
        jdbcTemplate.update(query,
                hullClassification.getDesignation(), hullClassification.getMinimumRankPrecedence(),
                abbreviation);
    }

    @Override
    public void delete(String abbreviation) {
        String query = "DELETE FROM hull_classification WHERE abbreviation = ?";
        this.jdbcTemplate.update(query, abbreviation);
    }
}
