package com.codecool.fleetmanager.model.mapper;

import com.codecool.fleetmanager.model.Country;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;


@Component
public class CountryMapper implements RowMapper<Country> {
    @Override
    public Country mapRow(ResultSet rs, int rowNum) throws SQLException {
        Country country = new Country();
        country.setId(rs.getLong("id"));
        country.setName(rs.getString("name"));
        return country;
    }
}
