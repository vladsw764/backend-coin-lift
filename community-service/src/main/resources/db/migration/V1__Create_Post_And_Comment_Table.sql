CREATE TABLE posts (
    id UUID default gen_random_uuid() NOT NULL,
    content VARCHAR(500) NOT NULL,
    title VARCHAR(75),
    image_link VARCHAR(20),
    created_at TIMESTAMP(6),
    updated_at TIMESTAMP(6),
    PRIMARY KEY (id)
);

CREATE TABLE comments (
    id UUID default gen_random_uuid() NOT NULL,
    content VARCHAR(500) NOT NULL,
    created_at TIMESTAMP(6),
    updated_at TIMESTAMP(6),
    post_id UUID,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS comments
    ADD CONSTRAINT FK_POSTS_COMMENTS FOREIGN KEY (post_id) REFERENCES posts(id);
