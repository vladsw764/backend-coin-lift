ALTER TABLE IF EXISTS users
    ADD COLUMN image_url VARCHAR(40);

UPDATE users
SET image_url = 'af5be274-71e7-4561-98fd-b33f80f759cf'
WHERE image_url IS NULL;

