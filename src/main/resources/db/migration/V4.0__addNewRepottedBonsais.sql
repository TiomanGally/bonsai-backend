INSERT INTO bonsais (uuid, latin_name, simple_name, birth_date, price, last_repoted)
VALUES (gen_random_uuid(), 'Acer Palmatum', null, '2021-03-07', 5.80, '2025-03-01'),
       (gen_random_uuid(), 'Acer Palmatum', null, '2021-03-07', 5.80, '2025-03-01'),
       (gen_random_uuid(), 'Acer', 'Noch nicht identifiziert', '2024-02-15', 0.00, '2025-03-02'),
       (gen_random_uuid(), 'Ficus', 'Carolins Baum vom Glas', '2021-01-01', 0.00, '2025-03-02'),
       (gen_random_uuid(), 'Picea Abies', 'Rotfichte', '2020-01-01', 55.00, '2025-03-03'),
       (gen_random_uuid(), 'Acer Palmatum Deshojo', 'Geplant für Othmar Auer Schale', '2021-03-07', 11.80,
        '2025-03-11'),
       (gen_random_uuid(), 'Crataegus monogyna', 'Weißdorn', '2021-03-07', 28.00, '2023-03-01'),
       (gen_random_uuid(), 'Acer Saccharinum', 'Silberahorn', '2024-02-15', 98.00, '2023-03-02'),
       (gen_random_uuid(), 'Acer Palmatum Koto-no-ito', null, '2021-01-01', 128.00, '2025-03-10'),
       (gen_random_uuid(), 'Acer Palmatum Deshojo', null, '2020-01-01', 135.00, '2023-03-03'),
       (gen_random_uuid(), 'Acer Palmatum', 'Vom Armbruster', '2020-01-01', 270.00, '2023-03-03'),
       (gen_random_uuid(), 'Ficus', 'Vom Armbruster', '2020-01-01', 170.80, '2023-03-03'),
       (gen_random_uuid(), 'Juniperus Itoigawa', 'Komische Gestaltung', '2020-01-01', 69.80, '2025-03-10'),
       (gen_random_uuid(), 'Larix Decidua', 'Klein', '2020-01-01', 5.80, '2023-03-03'),
       (gen_random_uuid(), 'Larix Decidua', 'Klein', '2020-01-01', 5.80, '2023-03-03'),
       (gen_random_uuid(), 'Larix Decidua', 'Klein', '2020-01-01', 5.80, '2025-03-04'),
       (gen_random_uuid(), 'Acer Palmatum Orangeola', null, '2021-03-07', 40.80, '2023-03-01'),
       (gen_random_uuid(), 'Larix Decidua', 'Aus Esslingen', '2021-03-07', 28.00, '2023-03-01'),
       (gen_random_uuid(), 'Acer Palmatum', '25 € Ahorn', '2024-02-15', 98.00, '2024-01-02'),
       (gen_random_uuid(), 'Pinus', 'Von Gerald', '2021-03-07', 0.00, '2025-03-05'),
       (gen_random_uuid(), 'Pinus strobus', 'Seidenkiefer', '2024-05-10', 55.00, '2020-01-01'),
       (gen_random_uuid(), 'Larix Kaempferi', 'Von Gerald', '2021-03-07', 0.00, '2025-03-14'),
       (gen_random_uuid(), 'Larix Decidua', 'Wäldchen von Gerald', '2021-03-07', 0.00, '2025-03-06');

with larix as (select uuid from bonsais where latin_name = 'Larix Decidua' and simple_name = 'Wäldchen von Gerald')
insert
into notes (uuid, content, created_at, bonsai_uuid)
select gen_random_uuid(),
       'Starker Lausbefall. Wurde entfernt und mit Lausmittel eingesprüht',
       current_date,
       larix.uuid
from larix;