package com.ecommerce.profiles.mapper;

import com.ecommerce.profiles.entity.Country;
import com.ecommerce.profiles.rest.dto.CountryResponse;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface CountryMapper {

    List<CountryResponse> toResponseList(List<Country> countries);

}
