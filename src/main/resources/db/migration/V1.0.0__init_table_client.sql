create table if not exists collaborator
(
    email     varchar primary key,
    lastName  varchar,
    firstName varchar,
    address   varchar,
    phone     varchar(10)

);