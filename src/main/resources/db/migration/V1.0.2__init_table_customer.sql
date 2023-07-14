create table if not exists customer
(
    customer_id BIGSERIAL primary key,
    customer_email varchar ,
    customer_name  varchar,
    customer_address varchar,
    customer_phone   varchar(10),
    customer_tva_number varchar(13)
);