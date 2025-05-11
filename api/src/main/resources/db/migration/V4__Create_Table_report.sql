CREATE TABLE IF NOT EXISTS onefreelance.report
(
    id uuid NOT NULL,
    report_data jsonb NOT NULL,
    create_at timestamp NOT NULL,
    CONSTRAINT report_pkey PRIMARY KEY(id)
);