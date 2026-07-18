package com.ecommerce.profiles.service.impl;

import com.ecommerce.profiles.mapper.CountryMapper;
import com.ecommerce.profiles.repository.CountryRepository;
import com.ecommerce.profiles.rest.dto.CountryListResponse;
import com.ecommerce.profiles.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class CountryServiceImpl implements CountryService {

    private final CountryMapper countryMapper;
    private final CountryRepository countryRepository;

    @Override
    public CountryListResponse listCountries() {
        var countries = countryRepository.findAll();
        var responseList = countryMapper.toResponseList(countries);
        return new CountryListResponse(responseList);
    }

}
