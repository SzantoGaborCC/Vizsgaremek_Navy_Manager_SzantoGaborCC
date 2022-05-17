package com.codecool.navymanager.model.mapper;

import com.codecool.navymanager.model.HullClassification;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;


@Component
public class HullClassificationMapper implements RowMapper<HullClassification> {
    @Override
    public HullClassification mapRow(ResultSet rs, int rowNum) throws SQLException {
        HullClassification hullClassification = new HullClassification();
        hullClassification.setAbbreviation(rs.getString("abbreviation"));
        hullClassification.setDesignation(rs.getString("designation"));
        hullClassification.setMinimumRankPrecedence(rs.getInt("minimum_rank_precedence"));
        return hullClassification;
    }
}
