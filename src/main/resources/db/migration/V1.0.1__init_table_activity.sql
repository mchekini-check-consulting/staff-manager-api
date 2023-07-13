create table if not exists activity (
    id serial primary key,
    collaborator_id bigint,
    date date,
    quantity int,
    category smallint,
    comment varchar(255)
);