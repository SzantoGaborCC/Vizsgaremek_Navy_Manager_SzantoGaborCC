package com.codecool.navymanager.service;

import com.codecool.navymanager.DTO.RankDTO;
import com.codecool.navymanager.dao.RankDao;
import com.codecool.navymanager.entityDTO.CountryDto;
import com.codecool.navymanager.entityDTO.RankDto;
import com.codecool.navymanager.repository.RankRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RankService {
    private final RankRepository rankRepository;

    public RankService(RankRepository rankRepository) {
        this.rankRepository = rankRepository;
    }

    public List<RankDto> findAll() {
        return rankRepository.findAll().stream()
                .map(rank -> new RankDto(rank))
                .toList();
    }

    public RankDto findByPrecedence(int rankPrecedence) {
        return new RankDto(rankRepository.findByPrecedence(rankPrecedence).orElseThrow());
    }

    @Transactional
    public void add(RankDto rankDto) {
        rankRepository.save(rankDto);
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
