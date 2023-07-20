CREATE TABLE auth_token
(
    id         BIGSERIAL   NOT NULL,
    token      TEXT        NOT NULL,
    token_type VARCHAR(30) NOT NULL,
    expired    BOOLEAN     NOT NULL,
    revoked    BOOLEAN     NOT NULL,
    user_id    UUID        NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE auth_token
    ADD CONSTRAINT FK_USER_TOKENS
        FOREIGN KEY (user_id)
            REFERENCES users (id);