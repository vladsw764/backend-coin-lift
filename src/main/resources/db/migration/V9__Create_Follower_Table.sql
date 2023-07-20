CREATE TABLE followers
(
    id           UUID PRIMARY KEY,
    from_user_fk UUID NOT NULL,
    to_user_fk   UUID NOT NULL,
    CONSTRAINT fk_from_user FOREIGN KEY (from_user_fk) REFERENCES users (id),
    CONSTRAINT fk_to_user FOREIGN KEY (to_user_fk) REFERENCES users (id)
);

ALTER TABLE users
    ADD COLUMN followers_count INT DEFAULT 0,
    ADD COLUMN following_count INT DEFAULT 0;

CREATE INDEX idx_followers_from_user_fk ON followers (from_user_fk);
CREATE INDEX idx_followers_to_user_fk ON followers (to_user_fk);