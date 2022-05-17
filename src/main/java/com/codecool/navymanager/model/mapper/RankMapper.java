package com.codecool.navymanager.model.mapper;

import com.codecool.navymanager.model.Rank;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;


@Component
public class RankMapper implements RowMapper<Rank> {
    @Override
    public Rank mapRow(ResultSet rs, int rowNum) throws SQLException {
        Rank rank = new Rank();
        rank.setId(rs.getLong("id"));
        rank.setDesignation(rs.getString("designation"));
        rank.setPrecedence(rs.getInt("precedence"));
        return rank;
    }
}
