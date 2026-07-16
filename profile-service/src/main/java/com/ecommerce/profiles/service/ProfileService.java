package com.ecommerce.profiles.service;

import com.ecommerce.profiles.rest.dto.CreateProfileRequest;
import com.ecommerce.profiles.rest.dto.ProfileResponse;
import com.ecommerce.profiles.rest.dto.UpdateProfileRequest;

import java.util.UUID;

public interface ProfileService {

    ProfileResponse createProfile(CreateProfileRequest request);

    ProfileResponse getProfile(UUID profileId);

    ProfileResponse updateProfile(UUID profileId, UpdateProfileRequest request);

    void deleteProfile(UUID profileId);

}
