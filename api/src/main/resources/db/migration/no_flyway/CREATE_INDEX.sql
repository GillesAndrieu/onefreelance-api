/**
  * Customer
 */
CREATE INDEX CONCURRENTLY idx_customer_email ON onefreelance.customer((customer_data->>'email'));
/**
  * Client
 */
CREATE INDEX CONCURRENTLY idx_client_customer_id ON onefreelance.client((client_data->>'customer_id'));
/**
  * Contract
 */
CREATE INDEX CONCURRENTLY idx_contract_customer_id ON onefreelance.contract((contract_data->>'customer_id'));
/**
  * Report
 */
CREATE INDEX CONCURRENTLY idx_report_customer_id ON onefreelance.report((contract_data->>'customer_id'));
CREATE INDEX CONCURRENTLY idx_report_year ON onefreelance.report((contract_data->>'year'));
CREATE INDEX CONCURRENTLY idx_report_month ON onefreelance.report((contract_data->>'month'));
/**
  * Notify
 */
CREATE INDEX CONCURRENTLY idx_notify_customer_id ON onefreelance.notify((contract_data->>'customer_id'));