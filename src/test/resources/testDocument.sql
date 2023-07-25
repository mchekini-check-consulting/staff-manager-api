-- Insert three collaborators
INSERT INTO collaborator (firstName, lastName, address, email, phone)
VALUES
    ('John', 'Doe', 'New York City', 'john.doe@example.com', '1234567890'),
    ('Jane', 'Smith', 'Los Angeles', 'jane.smith@example.com', '9876543210'),
    ('Michael', 'Johnson', 'Chicago', 'michael.johnson@example.com', '5555555555');

-- Insert two documents for each collaborator
INSERT INTO document (name, type, collaborator_id, created_at)
VALUES
    ('Document 1', 'TRANSPORT', 1, '2023-07-23'),
    ('Document 2', 'CARTE_VITALE', 1, '2023-07-23'),
    ('Document 3', 'TRANSPORT', 2, '2023-07-23'),
    ('Document 4', 'CARTE_VITALE', 2, '2023-07-23'),
    ('Document 5', 'TRANSPORT', 3, '2023-07-23'),
    ('Document 6', 'CARTE_VITALE', 3, '2023-07-23');