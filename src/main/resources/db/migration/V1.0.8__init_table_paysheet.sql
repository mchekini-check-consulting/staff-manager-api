CREATE TABLE IF NOT EXISTS paysheet (
    id SERIAL PRIMARY KEY,
    month_year VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    collaborator BIGSERIAL NOT NULL,
    CONSTRAINT fk_paysheet_collaborator
        FOREIGN KEY(collaborator)
        REFERENCES collaborator(id)
);