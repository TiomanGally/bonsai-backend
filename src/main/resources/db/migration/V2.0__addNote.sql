-- Add Notes table itself
CREATE TABLE if not exists notes
(
    uuid        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    content     text NOT NULL,
    created_at  DATE NOT NULL    default now(),
    bonsai_uuid UUID,
    constraint fk_bonsai
        foreign key (bonsai_uuid)
            references bonsais (uuid)
);

-- To have some testdata which is actual realdata I insert notes which I would add anyway
INSERT INTO notes (content, bonsai_uuid, created_at)
select 'Durch einen Sturm ist der Baum von der Bank gefallen und dabei ist ein Ast abgebrochen und die Schale kaputt gegangen. Musste Slip-repotted werden in die Schale, welche ich von Herons Bonsai gekauft habe.',
       (select uuid from bonsais where latin_name = 'Acer Palmatum Koto-no-ito' and price = 128.00),
       '2024-08-21'::date;

INSERT INTO notes (content, bonsai_uuid, created_at)
select 'Starker Rückschnitt. Bis auf 2 Blattpaare um Verzweigung zu fördern.',
       (select uuid from bonsais where simple_name = 'First Bonsai' and latin_name = 'Ficus'),
       '2025-03-20'::date;

INSERT INTO notes (content, bonsai_uuid, created_at)
select 'Beim Umtopfen musste die Schale zerstört werden, da die Wurzeln sämtliches Volumen eingenommen haben. Nach einem starken Rückschnitt, wurde das Substrat nicht vollständig entfernt, da es sonst dem Baum sehr geschädigt hätte, wenn noch radikaler eingegriffen würde',
       (select uuid
        from bonsais
        where simple_name = 'Silberahorn'
          and latin_name = 'Acer Saccharinum'
          and price = '98.00'),
       '2025-03-20'::date;

with larix as (select uuid from bonsais where latin_name = 'Larix Decidua' and simple_name = 'Wäldchen von Gerald')
insert
into notes (uuid, content, created_at, bonsai_uuid)
select gen_random_uuid(),
       'Der stärkste Lausbefall, den ich jemals erlebt habe. Wurde mit Zahnbürste entfernt und zweimal mit chem. Mittel eingesprüht',
       current_date,
       larix.uuid
from larix;