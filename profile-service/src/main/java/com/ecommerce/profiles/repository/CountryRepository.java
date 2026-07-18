package com.ecommerce.profiles.repository;

import com.ecommerce.profiles.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Short> {

    Optional<Country> findByCode(String code);

}
