package com.codecool.navymanager.dao;

import com.codecool.navymanager.model.Gun;
import com.codecool.navymanager.model.mapper.GunMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class GunJdbcDao implements GunDao {
    private final JdbcTemplate jdbcTemplate;
    private final GunMapper gunMapper;

    public GunJdbcDao(JdbcTemplate jdbcTemplate, GunMapper gunMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.gunMapper = gunMapper;
    }

    @Override
    public List<Gun> findAll() {
        String query = "SELECT * FROM gun";
        return jdbcTemplate.query(query, gunMapper);
    }

    @Override
    public Optional<Gun> findById(long id) {
        String query = "SELECT * FROM gun WHERE id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(query, gunMapper, id));
    }

    @Override
    public List<Gun> findByCountry(long countryId) {
        String query = "SELECT * FROM gun WHERE country_id = ?";
        return jdbcTemplate.query(query, gunMapper, countryId);
    }

    @Override
    public void add(Gun gun) {
        String query = "INSERT INTO gun " +
                "(designation, " +
                "caliber_in_mms, " +
                "projectile_weight_in_kgs, " +
                "range_in_meters, " +
                "minimum_ship_displacement_in_tons, " +
                "country_id) VALUES (?,?,?,?,?,?)";
       jdbcTemplate.update(query,
               gun.getDesignation(), gun.getCaliberInMms(),
               gun.getProjectileWeightInKgs(), gun.getRangeInMeters(),
               gun.getMinimumShipDisplacementInTons(), gun.getCountryId());
    }

    @Override
    public void update(Gun gun, long id) {
        String query = "UPDATE gun SET " +
                "designation = ?, " +
                "caliber_in_mms = ?, " +
                "projectile_weight_in_kgs = ?, " +
                "range_in_meters = ?, " +
                "minimum_ship_displacement_in_tons = ?, " +
                "country_id = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(query,
                gun.getDesignation(), gun.getCaliberInMms(),
                gun.getProjectileWeightInKgs(), gun.getRangeInMeters(),
                gun.getMinimumShipDisplacementInTons(), gun.getCountryId(), id);
    }

    @Override
    public void delete(long id) {
        String query = "DELETE FROM gun WHERE id = ?";
        this.jdbcTemplate.update(query, id);
    }
}
