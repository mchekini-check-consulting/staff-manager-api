create table if not exists mission
(
    id SERIAL NOT NULL primary key,
    name_mission varchar NOT NULL,
    starting_date_mission DATE NOT NULL,
    ending_date_mission DATE NOT NULL,
    collaborator_id BIGINT,
    customer_id BIGINT,
    customer_contact_lastname varchar NOT NULL,
    customer_contact_firstname varchar NOT NULL,
    customer_contact_email varchar NOT NULL,
    customer_contact_phone varchar NOT NULL,
    Mission_description varchar,

    CONSTRAINT fk_customer
        FOREIGN KEY(customer_id)
        REFERENCES customer(customer_id),

    CONSTRAINT fk_collaborator
        FOREIGN KEY(collaborator_id)
        REFERENCES collaborator(id)
);