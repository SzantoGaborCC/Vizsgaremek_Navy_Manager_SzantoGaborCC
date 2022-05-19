package com.codecool.navymanager.dao;

import com.codecool.navymanager.model.Rank;
import com.codecool.navymanager.model.mapper.RankMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RankJdbcDao implements RankDao {
    private final JdbcTemplate jdbcTemplate;
    private final RankMapper rankMapper;

    public RankJdbcDao(JdbcTemplate jdbcTemplate, RankMapper rankMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rankMapper = rankMapper;
    }

    @Override
    public List<Rank> findAll() {
        String query = "SELECT * FROM rank";
        return jdbcTemplate.query(query, rankMapper);
    }

    @Override
    public Optional<Rank> findById(long id) {
        String query = "SELECT * FROM rank WHERE id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(query, rankMapper, id));
    }

    @Override
    public Optional<Rank> findByPrecedence(int minimumRankPrecedence) {
        String query = "SELECT * FROM rank WHERE precedence = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(query, rankMapper, minimumRankPrecedence));
    }

    @Override
    public void add(Rank rank) {
        String query = "INSERT INTO rank " +
                "(designation, " +
                "precedence) " +
                "VALUES (?,?)";
       jdbcTemplate.update(query,
              rank.getDesignation(), rank.getPrecedence());
    }

    @Override
    public void update(Rank rank, long id) {
        String query = "UPDATE rank SET " +
                "designation = ?, " +
                "precedence = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(query,
                rank.getDesignation(), rank.getPrecedence(), id);
    }

    @Override
    public void delete(long id) {
        String query = "DELETE FROM rank WHERE id = ?";
        this.jdbcTemplate.update(query, id);
    }
}
