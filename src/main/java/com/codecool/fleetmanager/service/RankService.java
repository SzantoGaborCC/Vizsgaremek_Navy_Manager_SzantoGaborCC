package com.codecool.fleetmanager.service;

import com.codecool.fleetmanager.dao.RankDao;
import com.codecool.fleetmanager.model.Rank;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
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

    @Transactional
    public void add(Rank rank) {
        rankDao.add(rank);
    }

    @Transactional
    public void update(Rank rank, long id) {
        rankDao.update(rank, id);
    }

    @Transactional
    public void delete(long id) {
        rankDao.delete(id);
    }
}
