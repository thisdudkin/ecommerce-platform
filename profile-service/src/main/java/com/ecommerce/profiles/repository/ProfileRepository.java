package com.ecommerce.profiles.repository;

import com.ecommerce.profiles.entity.Profile;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ProfileRepository extends JpaRepository<Profile, UUID> {

    boolean existsByUserId(UUID userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Profile p WHERE p.id = :profileId")
    Optional<Profile> findByIdForUpdate(UUID profileId);

    @Query("""
            SELECT p
              FROM Profile p
                LEFT JOIN FETCH p.country
              WHERE p.id = :profileId
            """)
    Optional<Profile> findByIdWithCountry(UUID profileId);

}
