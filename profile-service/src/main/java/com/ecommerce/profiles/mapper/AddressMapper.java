package com.ecommerce.profiles.mapper;

import com.ecommerce.profiles.entity.Address;
import com.ecommerce.profiles.rest.dto.AddressResponse;
import com.ecommerce.profiles.rest.dto.CreateAddressRequest;
import com.ecommerce.profiles.rest.dto.UpdateAddressRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.Instant;
import java.time.OffsetDateTime;

import static java.time.ZoneOffset.UTC;
import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
        componentModel = SPRING,
        injectionStrategy = CONSTRUCTOR,
        uses = CountryMapper.class
)
public interface AddressMapper {

    @Mapping(target = "profileId", source = "profile.id")
    AddressResponse toResponse(Address address);

    @Mapping(target = "profile", ignore = true)
    @Mapping(target = "country", ignore = true)
    @Mapping(target = "version", ignore = true)
    Address toEntity(CreateAddressRequest request);

    @Mapping(target = "profile", ignore = true)
    @Mapping(target = "country", ignore = true)
    @Mapping(target = "version", ignore = true)
    void update(@MappingTarget Address address, UpdateAddressRequest request);

    default OffsetDateTime toOffsetDateTime(Instant timestamp) {
        if (timestamp == null) {
            return null;
        }
        return timestamp.atOffset(UTC);
    }

}
