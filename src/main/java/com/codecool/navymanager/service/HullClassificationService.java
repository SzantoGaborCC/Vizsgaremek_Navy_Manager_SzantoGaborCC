package com.codecool.navymanager.service;

import com.codecool.navymanager.entityDTO.HullClassificationDto;
import com.codecool.navymanager.repository.HullClassificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class HullClassificationService {

    private final HullClassificationRepository hullClassificationRepository;

    public HullClassificationService(HullClassificationRepository hullClassificationRepository) {
        this.hullClassificationRepository = hullClassificationRepository;
    }

    public List<HullClassificationDto> findAll() {
        return hullClassificationRepository.findAll().stream().map(HullClassificationDto::new).toList();
    }

    public HullClassificationDto findByAbbreviation(String abbreviation) {
        return new HullClassificationDto(hullClassificationRepository.findByAbbreviation(abbreviation));
    }

    @Transactional
    public void add(HullClassificationDto hullClassificationDto) {
        hullClassificationRepository.save(hullClassificationDto.toEntity());
    }

    @Transactional
    public void update(HullClassificationDto hullClassificationDto, String abbreviation) {
        if (hullClassificationRepository.existsByAbbreviation(abbreviation)) {
            hullClassificationRepository.save(hullClassificationDto.toEntity());
        } else {
            throw new IllegalArgumentException("No such Gun to update!");
        }
    }

    @Transactional
    public void deleteByAbbreviation(String abbreviation) {
        hullClassificationRepository.deleteByAbbreviation(abbreviation);
    }
}
