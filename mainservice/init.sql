CREATE TABLE IF NOT EXISTS bet
(
    id      uuid PRIMARY KEY DEFAULT gen_random_uuid() NOT NULL,
    game_id uuid                                       NOT NULL,
    user_id uuid                                       NOT NULL,
    type    character varying(1000)                    NOT NULL,
    value   character varying(1000)
);

CREATE TABLE IF NOT EXISTS price_game
(
    id            uuid PRIMARY KEY DEFAULT gen_random_uuid() NOT NULL,
    instrument    character varying(1000)                    NOT NULL,
    date_started  timestamp                                  NOT NULL,
    date_finished timestamp                                  NOT NULL,
    is_finished   boolean                                    NOT NULL
);

CREATE TABLE IF NOT EXISTS price
(
    id         uuid PRIMARY KEY DEFAULT gen_random_uuid() NOT NULL,
    instrument character varying(1000)                    NOT NULL,
    time       timestamp                                  NOT NULL,
    value      double precision                           NOT NULL
);

CREATE TABLE IF NOT EXISTS chat_post
(
    id      uuid PRIMARY KEY DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid                                       NOT NULL,
    type_id integer                                    NOT NULL
);