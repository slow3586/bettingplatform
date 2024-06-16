CREATE TABLE auth
(
    id       uuid PRIMARY KEY DEFAULT gen_random_uuid() NOT NULL,
    user_id  uuid UNIQUE                                NOT NULL,
    login    varchar(1000),
    password varchar(1000),
    role     varchar(1000)
);

CREATE TABLE customer
(
    id      uuid PRIMARY KEY DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid UNIQUE      NOT NULL,
    name    varchar(1000),
    email   varchar(1000),
    balance double precision
);