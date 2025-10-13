package com.recceasy.idp.layers.country;


import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/country")
@RestController
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    public List<Country> listCountries() {
        return countryService.findAll();
    }

    @PostMapping("/create")
    public Country createCountry(@RequestBody Country country) {
        return countryService.save(country);
    }

}
