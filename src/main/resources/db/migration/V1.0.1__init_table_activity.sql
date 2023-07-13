create table if not exists activity (
    id serial primary key,
    collaborator_id bigint,
    date date,
    quantity int,
    category varchar,
    comment varchar(255)
);