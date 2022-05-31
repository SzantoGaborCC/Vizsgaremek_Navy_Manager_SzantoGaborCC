package com.codecool.navymanager.service;


import com.codecool.navymanager.entityDTO.GunDto;
import com.codecool.navymanager.repository.GunRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class GunService {
    private final GunRepository gunRepository;

    public GunService(GunRepository gunRepository) {
        this.gunRepository = gunRepository;
    }

    public List<GunDto> findAll() {
        return gunRepository.findAll().stream().map(GunDto::new).toList();
    }

    public GunDto findById(long id) {
        return new GunDto(gunRepository.findById(id).orElseThrow());
    }

    public List<GunDto> findByCountry(long countryId)  {
        return gunRepository.findByCountryId(countryId).stream().map(GunDto::new).toList();
    }

    @Transactional
    public void save(GunDto gunDto) {
       gunRepository.save(gunDto.toEntity());
    }

    @Transactional
    public void delete(long id) {
        gunRepository.deleteById(id);
    }
}
