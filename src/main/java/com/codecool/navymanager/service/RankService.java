package com.codecool.navymanager.service;

import com.codecool.navymanager.dto.RankDto;
import com.codecool.navymanager.entity.Rank;
import com.codecool.navymanager.repository.RankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

@Service

public class RankService {
    @Autowired
    private MessageSource messageSource;
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

    
    public void add(RankDto rankDto, Locale locale) {
        if (rankDto.getId() != null && rankRepository.existsById(rankDto.getId())) {
            throw new IllegalArgumentException(messageSource.getMessage(
                    "add_error_must_not_exist",
                    new Object[]{Rank.class.getSimpleName(), Rank.class.getSimpleName()},
                    locale));
        }
        rankRepository.save(rankDto.toEntity());
    }

    
    public void update(RankDto rankDto, long id, Locale locale) {
        if (rankDto.getId() == null || rankDto.getId() != id) {
            throw new IllegalArgumentException(messageSource.getMessage(
                    "update_error_id",
                    new Object[] {Rank.class.getSimpleName()},
                    locale));
        }
        if (rankRepository.existsById(id)) {
            rankRepository.save(rankDto.toEntity());
        } else {
            throw new IllegalArgumentException(messageSource.getMessage(
                    "update_error_must_exist",
                    new Object[] {Rank.class.getSimpleName(), Rank.class.getSimpleName()},
                    locale));
        }
    }

    
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
