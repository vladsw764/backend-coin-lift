ALTER TABLE IF EXISTS posts
    ADD COLUMN user_id UUID;

ALTER TABLE IF EXISTS comments
    ADD COLUMN user_id UUID;

ALTER TABLE IF EXISTS posts
    ADD CONSTRAINT FK_USERS_POSTS
        FOREIGN KEY (user_id)
            REFERENCES users (id);

ALTER TABLE comments
    ADD CONSTRAINT FK_USERS_COMMENTS
        FOREIGN KEY (user_id)
            REFERENCES users (id);
