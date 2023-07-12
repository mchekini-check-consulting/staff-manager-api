create table if not exists collaborator (
    id BIGSERIAL PRIMARY KEY,
    firstName varchar,
    lastName varchar,
    address varchar,
    email varchar,
    phone varchar(10)
);