CREATE TABLE customer.country
(
    id         smallint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name       varchar(100)                NOT NULL UNIQUE,
    code       varchar(2)                  NOT NULL UNIQUE,
    created_at timestamp without time zone NOT NULL DEFAULT now(),
    updated_at timestamp without time zone NOT NULL DEFAULT now()
);