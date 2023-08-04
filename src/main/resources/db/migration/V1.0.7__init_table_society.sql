create table if not exists society (
    id serial primary key,
    name varchar,
    siret varchar,
    vat varchar,
    contact varchar,
    email varchar,
    address varchar,
    capital varchar
);