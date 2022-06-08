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

    public HullClassificationDto findById(long id) {
        return new HullClassificationDto(hullClassificationRepository.findById(id));
    }

    @Transactional
    public void add(HullClassificationDto hullClassificationDto) {
        hullClassificationRepository.save(hullClassificationDto.toEntity());
    }

    @Transactional
    public void update(HullClassificationDto hullClassificationDto, long id) {
        if (hullClassificationRepository.existsById(id)) {
            hullClassificationRepository.save(hullClassificationDto.toEntity());
        } else {
            throw new IllegalArgumentException("No such Hull Classification to update!");
        }
    }

    @Transactional
    public void deleteById(long id) {
        hullClassificationRepository.deleteById(id);
    }
}
