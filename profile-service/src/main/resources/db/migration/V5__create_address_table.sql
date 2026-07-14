CREATE TABLE customer.address
(
    id         uuid PRIMARY KEY                     DEFAULT uuidv7(),
    profile_id uuid                        NOT NULL REFERENCES customer.profile ON DELETE CASCADE,
    country_id smallint                    NOT NULL REFERENCES customer.country,
    label      varchar(50)                 NOT NULL,
    recipient  varchar(200)                NOT NULL,
    region     varchar(100),
    city       varchar(100)                NOT NULL,
    zipcode    varchar(20),
    street     varchar(150)                NOT NULL,
    building   varchar(30)                 NOT NULL,
    apartment  varchar(30),
    preferred  boolean                     NOT NULL DEFAULT FALSE,
    version    bigint                      NOT NULL DEFAULT 0,
    created_at timestamp without time zone NOT NULL DEFAULT now(),
    updated_at timestamp without time zone NOT NULL DEFAULT now()
);