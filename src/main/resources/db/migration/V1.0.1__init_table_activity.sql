create table if not exists activity (
    id serial primary key,
    date date,
    quantity int,
    category smallint,
    comment varchar(255)
);