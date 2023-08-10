ALTER TABLE paysheet
    ADD COLUMN IF NOT EXISTS month INT,
    ADD COLUMN IF NOT EXISTS year INT;

UPDATE paysheet
SET
    month = CAST(SPLIT_PART(month_year, '/', 1) AS INT),
    year = CAST(SPLIT_PART(month_year, '/', 2) AS INT);
