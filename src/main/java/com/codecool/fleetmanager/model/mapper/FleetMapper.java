package com.codecool.fleetmanager.model.mapper;

import com.codecool.fleetmanager.model.Fleet;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;


@Component
public class FleetMapper implements RowMapper<Fleet> {
    @Override
    public Fleet mapRow(ResultSet rs, int rowNum) throws SQLException {
        Fleet fleet = new Fleet();
        fleet.setId(rs.getLong("id"));
        fleet.setDesignation(rs.getString("designation"));
        fleet.setMinimumRankPrecedence(rs.getInt("minimum_rank_precedence"));
        fleet.setCommanderId(rs.getLong("commander_id"));
        fleet.setCountryId(rs.getLong("country_id"));
        return fleet;
    }
}
