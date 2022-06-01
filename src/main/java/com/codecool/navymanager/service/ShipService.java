package com.codecool.navymanager.service;


import com.codecool.navymanager.entityDTO.CountryDto;
import com.codecool.navymanager.entityDTO.ShipDto;

import com.codecool.navymanager.repository.ShipRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ShipService {
    private final ShipRepository shipRepository;

    public ShipService(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    public List<ShipDto> findAll() {
        return shipRepository.findAll().stream().map(ShipDto::new).toList();
    }

    public ShipDto findById(long id) {
        return new ShipDto(shipRepository.findById(id).orElseThrow());
    }

    @Transactional
    public void save(ShipDto shipDto) {
        shipRepository.save(shipDto.toEntity());
    }

    @Transactional
    public void deleteById(long id) {
        shipRepository.deleteById(id);
    }

    public List<ShipDto> findByCountry(CountryDto countryDto)  {
        return shipRepository.findByCountry(countryDto.toEntity()).stream()
                .map(ShipDto::new).toList();
    }
}
