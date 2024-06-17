CREATE TABLE auth
(
    id       uuid PRIMARY KEY DEFAULT gen_random_uuid() NOT NULL,
    user_id  uuid UNIQUE                                NOT NULL,
    login    varchar(1000) UNIQUE                       NOT NULL,
    password varchar(1000)                              NOT NULL,
    role     varchar(1000)    DEFAULT 'user'            NOT NULL
);

CREATE TABLE customer
(
    id      uuid PRIMARY KEY DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid UNIQUE                                NOT NULL,
    name    varchar(1000)                              NOT NULL,
    email   varchar(1000),
    balance double precision DEFAULT 0                 NOT NULL
);