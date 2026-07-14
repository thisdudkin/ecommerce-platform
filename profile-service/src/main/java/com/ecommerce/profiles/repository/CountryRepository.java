package com.ecommerce.profiles.repository;

import com.ecommerce.profiles.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Short> {
}
