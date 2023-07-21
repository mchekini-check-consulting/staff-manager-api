CREATE TABLE IF NOT exists document (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    type VARCHAR(50),
    collaborator_id BIGSERIAL,
    created_at VARCHAR(20),
    CONSTRAINT fk_collaborator_document
            FOREIGN KEY(collaborator_id)
            REFERENCES collaborator(id)
);
