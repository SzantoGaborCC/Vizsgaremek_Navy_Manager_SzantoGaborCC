package com.codecool.navymanager.dao;

import com.codecool.navymanager.model.Rank;

import java.util.List;
import java.util.Optional;

public interface RankDao {
    List<Rank> findAll();

    Optional<Rank> findById(long id);

    Optional<Rank> findByPrecedence(int minimumRankPrecedence);

    void add(Rank rank);

    void update(Rank rank, long id);

    void delete(long id);
}