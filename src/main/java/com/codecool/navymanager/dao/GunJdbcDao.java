package com.codecool.navymanager.dao;

import com.codecool.navymanager.model.Gun;
import com.codecool.navymanager.model.mapper.GunMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class GunJdbcDao implements GunDao {
    private JdbcTemplate jdbcTemplate;
    private GunMapper gunMapper;

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
    public void add(Gun gun) {
        String query = "INSERT INTO gun " +
                "(designation, " +
                "caliber_in_mms, " +
                "projectile_weight_in_kgs, " +
                "range_in_meters) VALUES (?,?,?,?)";
       jdbcTemplate.update(query,
               gun.getDesignation(), gun.getCaliberInMms(),
               gun.getProjectileWeightInKgs(), gun.getRangeInMeters());
    }

    @Override
    public void update(Gun gun, long id) {
        String query = "UPDATE gun SET " +
                "designation = ?, " +
                "caliber_in_mms = ?, " +
                "projectile_weight_in_kgs = ?, " +
                "range_in_meters = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(query,
                gun.getDesignation(), gun.getCaliberInMms(),
                gun.getProjectileWeightInKgs(), gun.getRangeInMeters(), id);
    }

    @Override
    public void delete(long id) {
        String query = "DELETE FROM gun WHERE id = ?";
        this.jdbcTemplate.update(query, id);
    }
}
