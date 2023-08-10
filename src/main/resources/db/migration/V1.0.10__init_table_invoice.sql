CREATE TABLE Invoice (
    id SERIAL PRIMARY KEY ,
    name VARCHAR(255),
    collaborator_id BIGSERIAL,
    customer_id BIGSERIAL,
    created_at DATE,
    month_year DATE,
  CONSTRAINT fk_invoice_collaborator FOREIGN KEY(collaborator_id) REFERENCES collaborator(id),
  CONSTRAINT fk_invoice_customer FOREIGN KEY(customer_id) REFERENCES customer(customer_id)
);
