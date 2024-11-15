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

INSERT INTO notes (content, bonsai_uuid)
VALUES ('Wind broke down an important branch', '223cb895-4a15-49ec-b7f8-da23d7048bb1')