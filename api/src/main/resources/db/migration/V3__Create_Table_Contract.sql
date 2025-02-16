CREATE TABLE IF NOT EXISTS onefreelance.contract
(
    id uuid NOT NULL,
    contract_data jsonb NOT NULL,
    create_at timestamp NOT NULL,
    CONSTRAINT contract_pkey PRIMARY KEY(id)
);