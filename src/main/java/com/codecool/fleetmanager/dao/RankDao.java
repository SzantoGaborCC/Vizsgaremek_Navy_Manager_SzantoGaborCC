package com.codecool.fleetmanager.dao;

import com.codecool.fleetmanager.model.Rank;

import java.util.List;
import java.util.Optional;

public interface RankDao {
    List<Rank> findAll();

    Optional<Rank> findById(long id);

    void add(Rank rank);

    void update(Rank rank, long id);

    void delete(long id);
}
