package com.ecommerce.profiles.rest.controller;

import com.ecommerce.profiles.rest.api.ProfilesApi;
import com.ecommerce.profiles.rest.dto.CreateProfileRequest;
import com.ecommerce.profiles.rest.dto.ProfileResponse;
import com.ecommerce.profiles.rest.dto.UpdateProfileRequest;
import com.ecommerce.profiles.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ProfileRestController implements ProfilesApi {

    private final ProfileService profileService;

    @Override
    public ResponseEntity<ProfileResponse> createProfile(CreateProfileRequest createProfileRequest) {
        ProfileResponse response = profileService.createProfile(createProfileRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{profileId}")
                .buildAndExpand(response.getId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @Override
    public ResponseEntity<Void> deleteProfile(UUID profileId) {
        profileService.deleteProfile(profileId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<ProfileResponse> getProfile(UUID profileId) {
        ProfileResponse response = profileService.getProfile(profileId);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ProfileResponse> updateProfile(UUID profileId, UpdateProfileRequest updateProfileRequest) {
        ProfileResponse response = profileService.updateProfile(profileId, updateProfileRequest);
        return ResponseEntity.ok(response);
    }

}
