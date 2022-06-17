package com.codecool.navymanager.service;

import com.codecool.navymanager.dto.RankDto;
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

    public RankDto findById(long id) {
        return new RankDto(rankRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No such rank!")));
    }

    @Transactional
    public void add(RankDto rankDto) {
        rankRepository.save(rankDto.toEntity());
    }

    @Transactional
    public void update(RankDto rankDto, long id) {
        if (rankRepository.existsById(id)) {
            rankRepository.save(rankDto.toEntity());
        } else {
            throw new IllegalArgumentException("No such rank!");
        }
    }

    @Transactional
    public void deleteById(long id) {
        rankRepository.deleteById(id);
    }
}
