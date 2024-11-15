-- add new table users
CREATE TABLE users
(
    uuid  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name  text not null,
    email text
);

-- add two users. Me and my girlfriend.
INSERT INTO users (uuid, name, email)
VALUES ('223cb895-4a15-49ec-b7f8-da23d7048bb1', 'Tioman Gally', 'tioman@gally.de'),
       ('223cb895-4a15-49ec-b7f8-da23d7048bb2', 'Carolin Schlegel', 'carolin@schlegel.de');

-- add foreignKeyConstraint to bonsai
alter table bonsais
    add user_id uuid
        constraint bonsai_user_fk references users (uuid);

-- add carolins bonsais
insert into bonsais (uuid, latin_name, simple_name, birth_date, price, last_repoted, user_id)
VALUES (gen_random_uuid(), 'Prunus Spinosa', 'Schwarzdorn', '2021-03-07', 5.80, '2020-02-27',
        '223cb895-4a15-49ec-b7f8-da23d7048bb2'),
       (gen_random_uuid(), 'Azalee', 'Niko', '2021-03-07', 5.80, '2024-07-04', '223cb895-4a15-49ec-b7f8-da23d7048bb2'),
       (gen_random_uuid(), 'Chaenomeles Japonica', 'Zierquitte', '2021-03-07', 170.00, '2020-02-27',
        '223cb895-4a15-49ec-b7f8-da23d7048bb2');

-- all other bonsais belongs to me
update bonsais
set user_id = '223cb895-4a15-49ec-b7f8-da23d7048bb1'
where user_id is null;