package com.ecommerce.profiles.mapper;

import com.ecommerce.profiles.entity.Profile;
import com.ecommerce.profiles.rest.dto.CreateProfileRequest;
import com.ecommerce.profiles.rest.dto.ProfileResponse;
import com.ecommerce.profiles.rest.dto.UpdateProfileRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
        componentModel = SPRING,
        injectionStrategy = CONSTRUCTOR,
        uses = AddressMapper.class
)
public interface ProfileMapper {

    ProfileResponse toResponse(Profile profile);

    @Mapping(target = "country", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "addresses", ignore = true)
    Profile toEntity(CreateProfileRequest request);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "country", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "addresses", ignore = true)
    void update(@MappingTarget Profile profile, UpdateProfileRequest request);

}
