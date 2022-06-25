package com.codecool.navymanager.service;

import com.codecool.navymanager.dto.HullClassificationDto;
import com.codecool.navymanager.entity.Country;
import com.codecool.navymanager.entity.HullClassification;
import com.codecool.navymanager.repository.HullClassificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

@Service

public class HullClassificationService {
    @Autowired
    MessageSource messageSource;

    private final HullClassificationRepository hullClassificationRepository;

    public HullClassificationService(HullClassificationRepository hullClassificationRepository) {
        this.hullClassificationRepository = hullClassificationRepository;
    }

    public List<HullClassificationDto> findAll() {
        return hullClassificationRepository.findAll().stream().map(HullClassificationDto::new).toList();
    }

    public HullClassificationDto findById(long id, Locale locale) {
        return new HullClassificationDto(hullClassificationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(messageSource.getMessage(
                        "no_such",
                        new Object[] {HullClassification.class.getSimpleName()},
                        locale))));
    }

    
    public void add(HullClassificationDto hullClassificationDto, Locale locale) {
        if (hullClassificationDto.getId() != null && hullClassificationRepository.existsById(hullClassificationDto.getId())) {
            throw new IllegalArgumentException(messageSource.getMessage(
                    "add_error_must_not_exist",
                    new Object[]{HullClassification.class.getSimpleName(), HullClassification.class.getSimpleName()},
                    locale));
        }
        hullClassificationRepository.save(hullClassificationDto.toEntity());
    }

    
    public void update(HullClassificationDto hullClassificationDto, long id, Locale locale) {
        if (hullClassificationDto.getId() == null || hullClassificationDto.getId() != id) {
            throw new IllegalArgumentException(messageSource.getMessage(
                    "update_error_id",
                    new Object[] {HullClassification.class.getSimpleName()},
                    locale));
        }
        if (hullClassificationRepository.existsById(id)) {
            hullClassificationRepository.save(hullClassificationDto.toEntity());
        } else {
            throw new IllegalArgumentException(messageSource.getMessage(
                    "update_error_must_exist",
                    new Object[] {HullClassification.class.getSimpleName(), HullClassification.class.getSimpleName()},
                    locale));
        }
    }


    
    public void deleteById(long id, Locale locale) {
        if (hullClassificationRepository.existsById(id)) {
            hullClassificationRepository.deleteById(id);
        } else {
            throw new NoSuchElementException(messageSource.getMessage(
                    "delete_error_must_exist",
                    new Object[] {HullClassification.class.getSimpleName()},
                    locale));
        }
    }
}
