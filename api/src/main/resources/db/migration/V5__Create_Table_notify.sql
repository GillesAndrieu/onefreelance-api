CREATE TABLE IF NOT EXISTS onefreelance.notify
(
    id uuid NOT NULL,
    notify_data jsonb NOT NULL,
    create_at timestamp NOT NULL,
    CONSTRAINT notify_pkey PRIMARY KEY(id)
);