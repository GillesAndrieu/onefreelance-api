CREATE TABLE IF NOT EXISTS onefreelance.customer
(
    id uuid NOT NULL,
    profile jsonb NOT NULL,
    create_at timestamp NOT NULL,
    CONSTRAINT customer_pkey PRIMARY KEY(id)
);

CREATE SEQUENCE IF NOT EXISTS onefreelance.startup_id_generator;
