package com.codecool.navymanager.repository;

import com.codecool.navymanager.entity.Rank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RankRepository extends JpaRepository<Rank, Integer> {
    Optional<Rank> findByPrecedence(int precedence);

    boolean existsByPrecedence(int precedence);
    void deleteByPrecedence(int precedence);
}