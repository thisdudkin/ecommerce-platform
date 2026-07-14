CREATE TABLE customer.profile
(
    id         uuid PRIMARY KEY                     DEFAULT uuidv7(),
    user_id    uuid                        NOT NULL UNIQUE,
    name       varchar(100)                NOT NULL,
    surname    varchar(100)                NOT NULL,
    birthdate  date,
    phone      varchar(32),
    country_id smallint REFERENCES customer.country,
    version    bigint                      NOT NULL DEFAULT 0,
    created_at timestamp without time zone NOT NULL DEFAULT now(),
    updated_at timestamp without time zone NOT NULL DEFAULT now()
);