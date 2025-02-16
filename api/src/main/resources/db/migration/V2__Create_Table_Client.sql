CREATE TABLE IF NOT EXISTS onefreelance.client
(
    id uuid NOT NULL,
    client_data jsonb NOT NULL,
    create_at timestamp NOT NULL,
    CONSTRAINT client_pkey PRIMARY KEY(id)
);