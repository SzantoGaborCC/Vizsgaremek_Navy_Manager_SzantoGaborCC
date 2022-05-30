package com.codecool.navymanager.service;

import com.codecool.navymanager.DTO.RankDTO;
import com.codecool.navymanager.dao.RankDao;
import com.codecool.navymanager.entityDTO.RankDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RankService {
    private final RankDao rankDao;

    public RankService(RankDao rankDao) {
        this.rankDao = rankDao;
    }

    public List<RankDto> findAll() {
        return rankDao.findAll().stream().map(RankDTO::new).toList();
    }

    public RankDTO findByPrecedence(int rankPrecedence) {
        return new RankDTO(rankDao.findByPrecedence(rankPrecedence).orElseThrow());
    }

    @Transactional
    public void add(RankDTO rankDTO) {
        rankDao.add(rankDTO.convertToRank());
    }

    @Transactional
    public void update(RankDTO rankDTO, long id) {
        rankDao.update(rankDTO.convertToRank(), id);
    }

    @Transactional
    public void delete(long id) {
        rankDao.delete(id);
    }
}
