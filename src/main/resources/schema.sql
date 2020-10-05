CREATE TABLE IF NOT EXISTS persons (
    id STRING PRIMARY KEY,
    firstname STRING NOT NULL,
    lastname STRING NOT NULL,
    address STRING NULL,
    birthday TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    gender STRING NOT NULL,
    comment STRING NULL
);

INSERT INTO persons (id, firstname, lastname, address, birthday, gender, comment) VALUES
    ('1f544519-26d9-43c3-9576-0da50cc13b4e', 'Petr', 'Bouda', 'Prague', '1989-05-21', 'MALE', 'This is my very useful comment')
    ON CONFLICT (id)
    DO NOTHING;