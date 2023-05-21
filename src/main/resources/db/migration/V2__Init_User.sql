CREATE TABLE users
(
    id       UUID         NOT NULL,
    email    VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(255),
    username VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT UK_users_email UNIQUE (email);

ALTER TABLE IF EXISTS users
    ADD CONSTRAINT UK_users_username UNIQUE (username);
