package com.codecool.fleetmanager.service;

import com.codecool.fleetmanager.DTO.CountryDTO;
import com.codecool.fleetmanager.DTO.GunDTO;
import com.codecool.fleetmanager.dao.CountryDao;
import com.codecool.fleetmanager.dao.GunDao;
import com.codecool.fleetmanager.model.Gun;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class GunService {
    private final GunDao gunDao;
    private final CountryDao countryDao;

    public GunService(GunDao gunDao, CountryDao countryDao) {
        this.gunDao = gunDao;
        this.countryDao = countryDao;
    }

    public List<GunDTO> findAll() {
        return gunDao.findAll().stream().map(this::createGunDTOWithDependencies).toList();
    }

    public GunDTO findById(long id) {
        Gun gun = gunDao.findById(id).orElseThrow();
        return createGunDTOWithDependencies(gun);
    }

    private GunDTO createGunDTOWithDependencies(Gun gun) {
        GunDTO gunDTO = new GunDTO(gun);
        gunDTO.setCountryDTO(new CountryDTO(countryDao.findById(gun.getCountryId()).orElseThrow()));
        return gunDTO;
    }

    @Transactional
    public void add(GunDTO gunDTO) {
        Gun gun = gunDTO.convertToGun();

        gunDao.add(gun);
    }

    @Transactional
    public void update(Gun gun, long id) {
        gunDao.update(gun, id);
    }

    @Transactional
    public void delete(long id) {
        gunDao.delete(id);
    }
}
