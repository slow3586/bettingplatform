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

CREATE TABLE "order"
(
    id      uuid PRIMARY KEY DEFAULT gen_random_uuid() NOT NULL,
    login   varchar(1000)                              NOT NULL,
    product varchar(1000)                              NOT NULL,
    time    timestamp                                  NOT NULL
);

CREATE TABLE payment
(
    id     uuid PRIMARY KEY DEFAULT gen_random_uuid() NOT NULL,
    login  varchar(1000)                              NOT NULL,
    source varchar(1000)                              NOT NULL,
    value  double precision                           NOT NULL,
    time   timestamp                                  NOT NULL
);