package com.codecool.navymanager.model.mapper;

import com.codecool.navymanager.model.Ship;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;


@Component
public class ShipMapper implements RowMapper<Ship> {
    @Override
    public Ship mapRow(ResultSet rs, int rowNum) throws SQLException {
        Ship ship = new Ship();
        ship.setId(rs.getLong("id"));
        ship.setName(rs.getString("name"));
        ship.setShipClassId(rs.getLong("ship_class_id"));
        ship.setCaptainId(rs.getLong("captain_id"));
        ship.setCountryId(rs.getLong("country_id"));
        return ship;
    }
}
