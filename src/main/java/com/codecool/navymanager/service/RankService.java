package com.codecool.navymanager.service;

import com.codecool.navymanager.entityDTO.GunDto;
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
                .map(RankDto::new)
                .toList();
    }

    public RankDto findByPrecedence(int precedence) {
        return new RankDto(rankRepository.findByPrecedence(precedence).orElseThrow());
    }

    @Transactional
    public void add(RankDto rankDto) {
        rankRepository.save(rankDto.toEntity());
    }

    @Transactional
    public void update(RankDto rankDto, int precedence) {
        if (rankRepository.existsByPrecedence(precedence)) {
           rankRepository.save(rankDto.toEntity());
        } else {
            throw new IllegalArgumentException("No such Rank to update!");
        }
    }

    @Transactional
    public void deleteByPrecedence(int precedence) {
        rankRepository.deleteByPrecedence(precedence);
    }
}
