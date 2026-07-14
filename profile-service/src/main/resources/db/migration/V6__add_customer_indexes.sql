CREATE INDEX ix_profile_country
    ON customer.profile (country_id);

CREATE INDEX ix_address_profile
    ON customer.address (profile_id);

CREATE INDEX ix_address_country
    ON customer.address (country_id);

CREATE UNIQUE INDEX ux_address_preferred
    ON customer.address (profile_id)
    WHERE preferred;