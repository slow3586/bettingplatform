CREATE TABLE IF NOT EXISTS bets
(
    id      uuid PRIMARY KEY DEFAULT gen_random_uuid() NOT NULL,
    user_id uuid                                       NOT NULL,
    type    character varying(1000)                    NOT NULL,
    value   character varying(1000)
);

CREATE TABLE IF NOT EXISTS games
(
    id            uuid PRIMARY KEY DEFAULT gen_random_uuid() NOT NULL,
    instrument    character varying(1000)                    NOT NULL,
    date_started  timestamp                                  NOT NULL,
    date_finished timestamp                                  NOT NULL,
    is_finished   boolean                                    NOT NULL
);