package com.codecool.navymanager.service;

import com.codecool.navymanager.entity.Officer;
import com.codecool.navymanager.entityDTO.OfficerDto;
import com.codecool.navymanager.repository.OfficerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OfficerService {
    private final OfficerRepository officerRepository;

    public OfficerService(OfficerRepository officerRepository) {
        this.officerRepository = officerRepository;
    }

    public List<OfficerDto> findAll() {
        return officerRepository.findAll().stream().map(OfficerDto::new).toList();
    }

    public OfficerDto findById(long id) {
        return new OfficerDto(officerRepository.findById(id).orElseThrow());
    }

    @Transactional
    public void save(OfficerDto officerDto) {
        officerRepository.save(officerDto.toEntity());
    }

    @Transactional
    public void deleteById(long id) {
        officerRepository.deleteById(id);
    }
}
