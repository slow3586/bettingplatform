CREATE TABLE bet
(
    id         uuid PRIMARY KEY DEFAULT gen_random_uuid() NOT NULL,
    game_id    uuid                                       NOT NULL,
    user_id    uuid                                       NOT NULL,
    status     varchar(1000)                              NOT NULL,
    type_id    int                                        NOT NULL,
    created_at timestamp        default CURRENT_TIMESTAMP NOT NULL,
    amount     bigint
);

CREATE TABLE game
(
    id             uuid PRIMARY KEY DEFAULT gen_random_uuid() NOT NULL,
    instrument     varchar(1000)                              NOT NULL,
    created_at     timestamp        default CURRENT_TIMESTAMP NOT NULL,
    start_at       timestamp                                  NOT NULL,
    finish_at      timestamp                                  NOT NULL,
    choice0        varchar(1000)                              NOT NULL,
    choice1        varchar(1000)                              NOT NULL,
    choice2        varchar(1000),
    winning_choice int,
    status         varchar(1000)                              NOT NULL
);

CREATE TABLE price
(
    id         uuid PRIMARY KEY DEFAULT gen_random_uuid() NOT NULL,
    instrument varchar(1000)                              NOT NULL,
    time       timestamp        default CURRENT_TIMESTAMP NOT NULL,
    value      double precision                           NOT NULL
);

CREATE TABLE chat_post
(
    id         uuid PRIMARY KEY DEFAULT gen_random_uuid() NOT NULL,
    created_at timestamp        default CURRENT_TIMESTAMP NOT NULL,
    user_id    uuid                                       NOT NULL,
    type_id    integer                                    NOT NULL,
    status     varchar(1000)                              NOT NULL
);

CREATE TABLE trace
(
    id                uuid PRIMARY KEY DEFAULT gen_random_uuid() NOT NULL,
    time              timestamp                                  NOT NULL,
    service_name      varchar(1000),
    span_id           varchar(1000),
    trace_id          varchar(1000),
    event_id          varchar(1000),
    method_class      varchar(1000),
    method_name       varchar(1000),
    method_args       varchar(1000),
    method_result     varchar(1000),
    exception_class   varchar(1000),
    exception_message varchar(1000),
    exception_stack   varchar(8000)
);