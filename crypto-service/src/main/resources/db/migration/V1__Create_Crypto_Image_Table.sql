CREATE TABLE crypto_images
(
    id               BIGSERIAL PRIMARY KEY,
    crypto_id        INTEGER NOT NULL,
    crypto_image_url TEXT,
    crypto_name      VARCHAR(40)
);

CREATE INDEX idx_crypto_id
    ON crypto_images (crypto_id);