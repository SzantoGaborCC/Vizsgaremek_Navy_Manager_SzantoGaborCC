package com.codecool.fleetmanager.service;

import com.codecool.fleetmanager.dao.RankDao;
import com.codecool.fleetmanager.model.Rank;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankService {
    private RankDao rankDao;

    public RankService(RankDao rankDao) {
        this.rankDao = rankDao;
    }

    public List<Rank> findAll() {
        return rankDao.findAll();
    }

    public Rank findById(long id) {
        return rankDao.findById(id).orElseThrow();
    }

    public void add(Rank rank) {
        rankDao.add(rank);
    }

    public void update(Rank rank, long id) {
        rankDao.update(rank, id);
    }

    public void delete(long id) {
        rankDao.delete(id);
    }
}
