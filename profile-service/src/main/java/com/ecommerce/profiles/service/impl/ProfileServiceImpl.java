package com.ecommerce.profiles.service.impl;

import com.ecommerce.profiles.entity.Country;
import com.ecommerce.profiles.entity.Profile;
import com.ecommerce.profiles.exception.CountryNotFoundException;
import com.ecommerce.profiles.exception.ProfileAlreadyExistsException;
import com.ecommerce.profiles.exception.ProfileNotFoundException;
import com.ecommerce.profiles.mapper.ProfileMapper;
import com.ecommerce.profiles.repository.CountryRepository;
import com.ecommerce.profiles.repository.ProfileRepository;
import com.ecommerce.profiles.rest.dto.CreateProfileRequest;
import com.ecommerce.profiles.rest.dto.ProfileResponse;
import com.ecommerce.profiles.rest.dto.UpdateProfileRequest;
import com.ecommerce.profiles.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
class ProfileServiceImpl implements ProfileService {
    private static final String USER_ID_UNIQUE_CONSTRAINT = "profile_user_id_key";

    private final ProfileMapper profileMapper;
    private final ProfileRepository profileRepository;
    private final CountryRepository countryRepository;

    @Override
    @Transactional
    public ProfileResponse createProfile(CreateProfileRequest request) {
        if (profileRepository.existsByUserId(request.getUserId())) {
            throw new ProfileAlreadyExistsException(request.getUserId());
        }
        Country country = resolveCountry(request.getCountryCode());
        Profile profile = profileMapper.toEntity(request);
        profile.setCountry(country);
        try {
            Profile savedProfile = profileRepository.saveAndFlush(profile);
            return profileMapper.toResponse(savedProfile);
        } catch (DataIntegrityViolationException exception) {
            if (isUserIdUniqueConstraintViolation(exception)) {
                throw new ProfileAlreadyExistsException(request.getUserId(), exception);
            }
            throw exception;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ProfileResponse getProfile(UUID profileId) {
        return profileMapper.toResponse(getOrThrow(profileId));
    }

    @Override
    @Transactional
    public ProfileResponse updateProfile(UUID profileId, UpdateProfileRequest request) {
        Profile profile = getForUpdateOrThrow(profileId);
        Country country = resolveCountry(request.getCountryCode());
        profileMapper.update(profile, request);
        profile.setCountry(country);
        profileRepository.flush();
        return profileMapper.toResponse(profile);
    }

    @Override
    @Transactional
    public void deleteProfile(UUID profileId) {
        Profile profile = getForUpdateOrThrow(profileId);
        profileRepository.delete(profile);
    }

    Profile getOrThrow(UUID profileId) {
        return profileRepository.findByIdWithCountry(profileId)
                .orElseThrow(() -> new ProfileNotFoundException(profileId));
    }

    Profile getForUpdateOrThrow(UUID profileId) {
        return profileRepository.findByIdForUpdate(profileId)
                .orElseThrow(() -> new ProfileNotFoundException(profileId));
    }

    Country resolveCountry(String code) {
        if (code == null) {
            return null;
        }
        return countryRepository.findByCode(code)
                .orElseThrow(() -> new CountryNotFoundException(code));
    }

    boolean isUserIdUniqueConstraintViolation(DataIntegrityViolationException exception) {
        Throwable cause = exception;
        while (cause != null) {
            if (cause instanceof ConstraintViolationException constraintViolation) {
                return USER_ID_UNIQUE_CONSTRAINT.equals(constraintViolation.getConstraintName());
            }
            cause = cause.getCause();
        }
        return false;
    }

}
