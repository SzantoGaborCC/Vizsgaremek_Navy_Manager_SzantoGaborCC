package com.codecool.navymanager.model.mapper;

import com.codecool.navymanager.model.Officer;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;


@Component
public class OfficerMapper implements RowMapper<Officer> {
    @Override
    public Officer mapRow(ResultSet rs, int rowNum) throws SQLException {
        Officer officer = new Officer();
        officer.setId(rs.getLong("id"));
        officer.setName(rs.getString("name"));
        officer.setDateOfBirth(rs.getDate("date_of_birth"));
        officer.setRank(rs.getInt("rank"));
        officer.setCountryId(rs.getLong("country_id"));
        return officer;
    }
}
