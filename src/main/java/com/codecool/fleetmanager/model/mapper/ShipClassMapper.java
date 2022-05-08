package com.codecool.fleetmanager.model.mapper;

import com.codecool.fleetmanager.model.ShipClass;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;


@Component
public class ShipClassMapper implements RowMapper<ShipClass> {
    @Override
    public ShipClass mapRow(ResultSet rs, int rowNum) throws SQLException {
        ShipClass shipClass = new ShipClass();
        shipClass.setId(rs.getLong("id"));
        shipClass.setName(rs.getString("name"));
        shipClass.setDisplacementInTons(rs.getInt("displacement_in_tons"));
        shipClass.setHullClassification(rs.getString("hull_classification"));
        shipClass.setArmorBeltInCms(rs.getInt("armor_belt_in_cms"));
        shipClass.setArmorTurretInCms(rs.getInt("armor_turret_in_cms"));
        shipClass.setArmorDeckInCms(rs.getInt("armor_deck_in_cms"));
        shipClass.setSpeedInKmh(rs.getInt("speed_in_kmh"));
        shipClass.setCountryId(rs.getLong("country_id"));
        return shipClass;
    }
}
