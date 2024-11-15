CREATE TABLE bonsais
(
    uuid         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    latin_name   VARCHAR(255)     NOT NULL,
    simple_name  VARCHAR(255),
    birth_date   DATE             NOT NULL,
    price        DOUBLE PRECISION NOT NULL,
    last_repoted DATE
);

INSERT INTO bonsais (uuid, latin_name, simple_name, birth_date, price, last_repoted)
VALUES ('223cb895-4a15-49ec-b7f8-da23d7048bb1', 'Acer Buergerianum', 'von Gerald', '2015-01-01', 0.00, '2025-02-27'),
       ('223cb895-4a15-49ec-b7f8-da23d7048bb2', 'Acer Palmatum', null, '2021-03-07', 5.80, '2025-02-27');