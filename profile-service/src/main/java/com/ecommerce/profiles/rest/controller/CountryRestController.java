package com.ecommerce.profiles.rest.controller;

import com.ecommerce.profiles.rest.api.CountriesApi;
import com.ecommerce.profiles.rest.dto.CountryListResponse;
import com.ecommerce.profiles.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CountryRestController implements CountriesApi {

    private final CountryService countryService;

    @Override
    public ResponseEntity<CountryListResponse> listCountries() {
        CountryListResponse response = countryService.listCountries();
        return ResponseEntity.ok(response);
    }

}
