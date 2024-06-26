CREATE TABLE auth
(
    id       uuid PRIMARY KEY DEFAULT gen_random_uuid() NOT NULL,
    login    varchar(1000) UNIQUE                       NOT NULL,
    password varchar(1000)                              NOT NULL,
    role     varchar(1000)                              NOT NULL,
    status   varchar(1000)                              NOT NULL
);

CREATE TABLE customer
(
    id          uuid PRIMARY KEY DEFAULT gen_random_uuid() NOT NULL,
    login       varchar(1000) UNIQUE                       NOT NULL,
    name        varchar(1000)                              NOT NULL,
    balance     double precision                           NOT NULL,
    status      varchar(1000)                              NOT NULL,
    has_premium boolean                                    NOT NULL
);