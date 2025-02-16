CREATE INDEX CONCURRENTLY idx_customer_email ON onefreelance.customer((customer_data->>'email'));
CREATE INDEX CONCURRENTLY idx_client_customer_id ON onefreelance.client((client_data->>'customer_id'));
CREATE INDEX CONCURRENTLY idx_contract_customer_id ON onefreelance.contract((contract_data->>'customer_id'));
