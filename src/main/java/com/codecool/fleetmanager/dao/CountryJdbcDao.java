package com.codecool.fleetmanager.dao;

import com.codecool.fleetmanager.model.Country;
import com.codecool.fleetmanager.model.mapper.CountryMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CountryJdbcDao implements CountryDao {
    JdbcTemplate jdbcTemplate;
    CountryMapper countryMapper;

    public CountryJdbcDao(JdbcTemplate jdbcTemplate, CountryMapper countryMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.countryMapper = countryMapper;
    }

    @Override
    public List<Country> findAll() {
        String query = "SELECT * FROM country";
        return jdbcTemplate.query(query, countryMapper);
    }

    @Override
    public Optional<Country> findById(long id) {
        String query = "SELECT * FROM country WHERE id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(query, countryMapper, id));
    }

    @Override
    public void add(Country country) {
        String query = "INSERT INTO country " +
                "(name) VALUES (?)";
       jdbcTemplate.update(query,
               country.getName());
    }

    @Override
    public void update(Country country, long id) {
        String query = "UPDATE country SET " +
                "name = ? WHERE id = ?";
        jdbcTemplate.update(query,
                country.getName(), id);
    }

    @Override
    public void delete(long id) {
        String query = "DELETE FROM country WHERE id = ?";
        this.jdbcTemplate.update(query, id);
    }
}
