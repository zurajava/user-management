create table if not exists redeemable
(
    id serial not null constraint redeemable_pk primary key,
    name    varchar not null,
    description varchar,
    image_url varchar,
    properties varchar,
    points_required_to_redeem integer,
    create_date timestamp default timezone('utc'::text, now()),
    last_modification_date timestamp
    );
alter table redeemable owner to pxelom;