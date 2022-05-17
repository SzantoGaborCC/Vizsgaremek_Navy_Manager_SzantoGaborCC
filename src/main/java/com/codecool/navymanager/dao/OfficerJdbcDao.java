package com.codecool.navymanager.dao;

import com.codecool.navymanager.model.Officer;
import com.codecool.navymanager.model.mapper.OfficerMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OfficerJdbcDao implements OfficerDao {
    JdbcTemplate jdbcTemplate;
    OfficerMapper officerMapper;

    public OfficerJdbcDao(JdbcTemplate jdbcTemplate, OfficerMapper officerMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.officerMapper = officerMapper;
    }

    @Override
    public List<Officer> findAll() {
        String query = "SELECT * FROM officer";
        return jdbcTemplate.query(query, officerMapper);
    }

    @Override
    public Optional<Officer> findById(long id) {
        String query = "SELECT * FROM officer WHERE id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(query, officerMapper, id));
    }

    @Override
    public void add(Officer officer) {
        String query = "INSERT INTO officer " +
                "(name, " +
                "date_of_birth, " +
                "rank_id, " +
                "country_id " +
                ") VALUES (?,?,?,?)";
       jdbcTemplate.update(query,
               officer.getName(), officer.getDateOfBirth(), officer.getRankId(), officer.getCountryId());
    }

    @Override
    public void update(Officer officer, long id) {
        String query = "UPDATE officer SET " +
                "name = ?, " +
                "date_of_birth = ?, " +
                "rank_id = ?, " +
                "country_id = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(query,
                officer.getName(), officer.getDateOfBirth(), officer.getRankId(), officer.getCountryId(), id);
    }

    @Override
    public void delete(long id) {
        String query = "DELETE FROM officer WHERE id = ?";
        this.jdbcTemplate.update(query, id);
    }
}
