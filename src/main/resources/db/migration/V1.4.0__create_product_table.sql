CREATE TABLE IF NOT EXISTS products
(
    id UUID PRIMARY KEY,
    name     TEXT           NOT NULL,
    price    DECIMAL(10, 2) NOT NULL,
    category TEXT
);

CREATE TABLE IF NOT EXISTS stores_products (
    id UUId PRIMARY KEY,
    id_store UUID NOT NULL,
    id_product UUID NOT NULL
);
