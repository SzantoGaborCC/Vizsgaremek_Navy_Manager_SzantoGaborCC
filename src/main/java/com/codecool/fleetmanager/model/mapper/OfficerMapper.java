package com.codecool.fleetmanager.model.mapper;

import com.codecool.fleetmanager.model.Officer;
import com.codecool.fleetmanager.model.ShipClass;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Spliterator;


@Component
public class OfficerMapper implements RowMapper<Officer> {
    @Override
    public Officer mapRow(ResultSet rs, int rowNum) throws SQLException {
        Officer officer = new Officer();
        officer.setId(rs.getLong("id"));
        officer.setDateOfBirth(rs.getDate("date_of_birth"));
        officer.setRankId(rs.getLong("rank_id"));
        officer.setCountryId(rs.getLong("country_id"));
        return officer;
    }
}
