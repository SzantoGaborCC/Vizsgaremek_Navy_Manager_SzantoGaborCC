package com.codecool.navymanager.service;

import com.codecool.navymanager.entityDTO.OfficerDto;
import com.codecool.navymanager.entityDTO.ShipDto;
import com.codecool.navymanager.repository.OfficerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    public List<OfficerDto> findAvailableOfficers(ShipDto shipDto)  {
        List<OfficerDto> foundOfficers = new ArrayList<>(officerRepository.findAvailableOfficersByCountry(shipDto.getCountry().toEntity()).stream()
                .map(OfficerDto::new).toList());
        if (shipDto.getCaptain() != null) {
            foundOfficers.add(0, shipDto.getCaptain());
        }
        foundOfficers.add(0, new OfficerDto(-1L, "----Unassigned----"));
        return foundOfficers;
    }

    @Transactional
    public void add(OfficerDto officerDto) {
        officerRepository.save(officerDto.toEntity());
    }

    @Transactional
    public void update(OfficerDto officerDto, long id) {
        if (officerRepository.existsById(id)) {
            officerRepository.save(officerDto.toEntity());
        } else {
            throw new IllegalArgumentException("No such Officer to update!");
        }
    }

    @Transactional
    public void deleteById(long id) {
        officerRepository.deleteById(id);
    }
}
