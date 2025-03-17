-- add new table users
CREATE TABLE users
(
    uuid  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name  text not null,
    email text
);

-- add two users. Me and my girlfriend.
insert into users (uuid, name, email)
values (gen_random_uuid(), 'Tioman Gally', 'tioman@gally.de'),
       (gen_random_uuid(), 'Carolin Schlegel', 'carolin@schlegel.de');

-- add foreignKeyConstraint to bonsai
alter table bonsais
    add user_id uuid
        constraint bonsai_user_fk references users (uuid);

-- add carolins bonsais
insert into bonsais (uuid, latin_name, simple_name, birth_date, price, last_repoted, user_id)
select gen_random_uuid(), -- will be revoked for every row
       bonsai.latin_name,
       bonsai.simple_name,
       bonsai.birth_date,
       bonsai.price,
       bonsai.last_repoted,
       u.uuid
from (select 'Prunus Spinosa'   AS latin_name,
             'Schwarzdorn'      AS simple_name,
             '2021-03-07'::DATE AS birth_date,
             5.80               AS price,
             '2025-03-15'::DATE AS last_repoted
      union all
      -- Using 'union all' instead of 'union' will not remove duplicates
      select 'Azalee', 'Niko', '2021-03-07', 5.80, '2024-07-04'
      union all
      select 'Chaenomeles Japonica', 'Zierquitte', '2021-03-07', 170.00, '2020-02-27') as bonsai
         -- we want only the user with this name and this email so this will be our join predicate
         inner join users u on u.name = 'Carolin Schlegel' and u.email = 'carolin@schlegel.de';

-- all other bonsais belongs to me
update bonsais
set user_id = (select uuid from users where name = 'Tioman Gally' and email = 'tioman@gally.de')
where user_id is null;