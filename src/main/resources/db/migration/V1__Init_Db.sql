CREATE SEQUENCE post_seq
    START WITH 1
    INCREMENT BY 1;

CREATE TABLE posts (
    id BIGINT NOT NULL,
    content TEXT NOT NULL,
    title VARCHAR(75) NOT NULL,
    image_link VARCHAR(255) NOT NULL,
    created_at TIMESTAMP(6),
    updated_at TIMESTAMP(6),
    PRIMARY KEY (id)
);

CREATE TABLE comments (
    uuid UUID NOT NULL,
    content VARCHAR(1000) NOT NULL,
    created_at TIMESTAMP(6),
    updated_at TIMESTAMP(6),
    post_id BIGINT,
    PRIMARY KEY (uuid)
);

ALTER TABLE IF EXISTS comment
    ADD CONSTRAINT FK_POSTS_COMMENTS FOREIGN KEY (post_id) REFERENCES posts;