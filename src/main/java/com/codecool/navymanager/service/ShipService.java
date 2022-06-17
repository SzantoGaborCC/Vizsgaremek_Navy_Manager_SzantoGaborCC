package com.codecool.navymanager.service;


import com.codecool.navymanager.entity.Ship;
import com.codecool.navymanager.dto.CountryDto;
import com.codecool.navymanager.dto.ShipDto;
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

    public List<ShipDto> findByCountry(CountryDto countryDto)  {
        return shipRepository.findByCountry(countryDto.toEntity()).stream()
                .map(ShipDto::new).toList();
    }

    public List<ShipDto> findAvailableShipsByCountry(CountryDto countryDto)  {
        return shipRepository.findAvailableShipsByCountry(countryDto.toEntity()).stream()
                .map(ShipDto::new).toList();
    }

    @Transactional
    public void add(ShipDto shipDto) {
        shipRepository.save(shipDto.toEntity());
    }

    @Transactional
    public void update(ShipDto shipDto, long id) {
        Ship oldShipData = shipRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No such ship!"));
        if (!shipDto.getCountry().getId().equals(oldShipData.getCountry().getId())) {
            shipDto.setCaptain(null);
        }
        Ship newShipData = shipDto.toEntity();
        newShipData.setFleet(oldShipData.getFleet());
        shipRepository.save(newShipData);
    }

    @Transactional
    public void deleteById(long id) {
        if (!shipRepository.existsById(id)) {
            throw new IllegalArgumentException("No such ship!");
        }
        shipRepository.deleteById(id);
    }
}
