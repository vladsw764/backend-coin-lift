ALTER TABLE IF EXISTS crypto_images
    ADD COLUMN crypto_name VARCHAR(40);

UPDATE crypto_images
SET crypto_name = CASE
                      WHEN crypto_id = 1 THEN 'btc-bitcoin'
                      WHEN crypto_id = 1027 THEN 'eth-ethereum'
                      WHEN crypto_id = 1839 THEN 'bnb-binance-coin'
                      WHEN crypto_id = 2010 THEN 'ada-cardano'
                      WHEN crypto_id = 52 THEN 'xrp-xrp'
    END;
