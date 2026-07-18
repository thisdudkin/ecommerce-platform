package com.ecommerce.profiles.repository;

import com.ecommerce.profiles.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {

    @Query("""
            SELECT a
              FROM Address a
                INNER JOIN FETCH a.profile
                INNER JOIN FETCH a.country
              WHERE a.id = :addressId
                AND a.profile.id = :profileId
            """)
    Optional<Address> findByIdAndProfileIdWithDetails(UUID profileId, UUID addressId);

    @Query("""
            SELECT a
              FROM Address a
                INNER JOIN FETCH a.profile
                INNER JOIN FETCH a.country
              WHERE a.profile.id = :profileId
              ORDER BY a.createdAt
            """)
    List<Address> findAllByProfileIdWithDetails(UUID profileId);

    long countByProfileId(UUID profileId);

    Optional<Address> findByProfileIdAndPreferredTrue(UUID profileId);

    Optional<Address> findFirstByProfileIdAndIdNotOrderByCreatedAtAsc(UUID profileId, UUID addressId);

}
