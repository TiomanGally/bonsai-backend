CREATE TABLE if not exists pictures
(
    uuid        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    file_name   text  NOT NULL,
    content     bytea NOT NULL,
    created_at  DATE  NOT NULL   default now(),
    bonsai_uuid UUID,
    constraint fk_bonsai
        foreign key (bonsai_uuid)
            references bonsais (uuid)
);
