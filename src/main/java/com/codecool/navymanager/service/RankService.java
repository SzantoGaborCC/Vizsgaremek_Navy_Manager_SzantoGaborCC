package com.codecool.navymanager.service;

import com.codecool.navymanager.dto.RankDto;
import com.codecool.navymanager.entity.Country;
import com.codecool.navymanager.entity.Rank;
import com.codecool.navymanager.repository.RankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class RankService {
    @Autowired
    MessageSource messageSource;
    private final RankRepository rankRepository;

    public RankService(RankRepository rankRepository) {
        this.rankRepository = rankRepository;
    }

    public List<RankDto> findAll() {
        return rankRepository.findAll().stream()
                .map(RankDto::new)
                .toList();
    }

    public RankDto findById(long id, Locale locale) {
        return new RankDto(rankRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage(
                        "no_such",
                        new Object[] {Rank.class.getSimpleName()},
                        locale))));
    }

    @Transactional
    public void add(RankDto rankDto) {
        rankRepository.save(rankDto.toEntity());
    }

    @Transactional
    public void update(RankDto rankDto, long id, Locale locale) {
        if (rankRepository.existsById(id)) {
            rankRepository.save(rankDto.toEntity());
        } else {
            throw new NoSuchElementException(messageSource.getMessage(
                    "no_such",
                    new Object[] {Rank.class.getSimpleName()},
                    locale));
        }
    }

    @Transactional
    public void deleteById(long id, Locale locale) {
        if (rankRepository.existsById(id)) {
            rankRepository.deleteById(id);
        } else {
            throw  new NoSuchElementException(
                    messageSource.getMessage(
                            "no_such",
                            new Object[] {Rank.class.getSimpleName()},
                            locale));
        }
    }
}
