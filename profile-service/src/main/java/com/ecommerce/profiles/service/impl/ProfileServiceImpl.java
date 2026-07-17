package com.ecommerce.profiles.service.impl;

import com.ecommerce.profiles.rest.dto.CreateProfileRequest;
import com.ecommerce.profiles.rest.dto.ProfileResponse;
import com.ecommerce.profiles.rest.dto.UpdateProfileRequest;
import com.ecommerce.profiles.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
class ProfileServiceImpl implements ProfileService {

    @Override
    public ProfileResponse createProfile(CreateProfileRequest request) {
        return null;
    }

    @Override
    public ProfileResponse getProfile(UUID profileId) {
        return null;
    }

    @Override
    public ProfileResponse updateProfile(UUID profileId, UpdateProfileRequest request) {
        return null;
    }

    @Override
    public void deleteProfile(UUID profileId) {

    }

}
