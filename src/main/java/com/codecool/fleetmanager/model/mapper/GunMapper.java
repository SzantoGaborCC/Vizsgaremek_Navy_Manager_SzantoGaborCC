package com.codecool.fleetmanager.model.mapper;

import com.codecool.fleetmanager.model.Gun;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;


@Component
public class GunMapper implements RowMapper<Gun> {
    @Override
    public Gun mapRow(ResultSet rs, int rowNum) throws SQLException {
        Gun gun = new Gun();
        gun.setId(rs.getLong("id"));
        gun.setDesignation(rs.getString("designation"));
        gun.setCaliberInMms(rs.getInt("caliber_in_mms"));
        gun.setProjectileWeightInKgs(rs.getInt("projectile_weight_in_kgs"));
        gun.setRangeInMeters(rs.getInt("range_in_meters"));
        gun.setMinimumShipDisplacementInTons(rs.getInt("minimum_ship_displacement_in_tons"));
        gun.setCountryId(rs.getLong("country_id"));
        return gun;
    }
}
