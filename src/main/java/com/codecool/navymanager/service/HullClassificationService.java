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
@Transactional(readOnly = true)
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

    @Transactional
    public void add(HullClassificationDto hullClassificationDto) {
        hullClassificationRepository.save(hullClassificationDto.toEntity());
    }

    @Transactional
    public void update(HullClassificationDto hullClassificationDto, long id, Locale locale) {
        if (hullClassificationRepository.existsById(id)) {
            hullClassificationRepository.save(hullClassificationDto.toEntity());
        } else {
            throw new NoSuchElementException(messageSource.getMessage(
                    "no_such",
                    new Object[] {HullClassification.class.getSimpleName()},
                    locale));
        }
    }


    @Transactional
    public void deleteById(long id, Locale locale) {
        if (hullClassificationRepository.existsById(id)) {
            hullClassificationRepository.deleteById(id);
        } else {
            throw  new NoSuchElementException(
                    messageSource.getMessage(
                            "no_such",
                            new Object[] {HullClassification.class.getSimpleName()},
                            locale));
        }
    }
}
