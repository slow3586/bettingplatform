CREATE TABLE IF NOT EXISTS "user"
(
    id       uuid PRIMARY KEY DEFAULT gen_random_uuid() NOT NULL,
    name     character varying(1000),
    email    character varying(1000),
    password character varying
);