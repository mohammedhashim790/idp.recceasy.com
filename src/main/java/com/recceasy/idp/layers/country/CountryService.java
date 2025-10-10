package com.recceasy.idp.layers.country;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CountryService {

    @Autowired
    private CountryRepository countryRepository;

    public List<Country> findAll() {
        return countryRepository.findAll();
    }

    public Optional<Country> findById(String countryCode) {
        return countryRepository.findById(countryCode);
    }

    public Country save(Country country) {
        if(countryRepository.existsById(country.getCountryCode())) {
            throw new DuplicateKeyException("Country already exists");
        }
        return countryRepository.save(country);
    }

    public Country update(Country country) {
        return countryRepository.save(country);
    }


}
