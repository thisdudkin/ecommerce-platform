package com.ecommerce.profiles.service.impl;

import com.ecommerce.profiles.repository.CountryRepository;
import com.ecommerce.profiles.rest.dto.CountryListResponse;
import com.ecommerce.profiles.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;

    @Override
    public CountryListResponse listCountries() {
        return null;
    }

}
