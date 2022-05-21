package com.codecool.navymanager.service;

import com.codecool.navymanager.DTO.GunDTO;
import com.codecool.navymanager.dao.GunDao;
import com.codecool.navymanager.model.Gun;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class GunService {
    private final GunDao gunDao;
    private final CountryService countryService;

    public GunService(GunDao gunDao, CountryService countryService) {
        this.gunDao = gunDao;
        this.countryService = countryService;
    }

    public List<GunDTO> findAll() {
        return gunDao.findAll().stream().map(this::createGunDTOWithDependencies).toList();
    }

    public GunDTO findById(long id) {
        Gun gun = gunDao.findById(id).orElseThrow();
        return createGunDTOWithDependencies(gun);
    }

    public List<GunDTO> findByCountry(long countryId)  {
        return gunDao.findByCountry(countryId).stream().map(this::createGunDTOWithDependencies).toList();
    }

    private GunDTO createGunDTOWithDependencies(Gun gun) {
        GunDTO gunDTO = new GunDTO(gun);
        gunDTO.setCountry(countryService.findById(gun.getCountryId()));
        return gunDTO;
    }


    @Transactional
    public void add(GunDTO gunDTO) {
        Gun gun = gunDTO.convertToGun();
        gunDao.add(gun);
    }

    @Transactional
    public void update(GunDTO gunDTO, long id) {
        System.out.println(gunDTO);
        gunDao.update(gunDTO.convertToGun(), id);
    }

    @Transactional
    public void delete(long id) {
        gunDao.delete(id);
    }
}
