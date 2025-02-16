CREATE TABLE IF NOT EXISTS onefreelance.customer
(
    id uuid NOT NULL,
    customer_data jsonb NOT NULL,
    create_at timestamp NOT NULL,
    CONSTRAINT customer_pkey PRIMARY KEY(id)
);