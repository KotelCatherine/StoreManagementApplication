CREATE TABLE IF NOT EXISTS stores (
    id UUID PRIMARY KEY,
    name TEXT NOT NULL,
    location TEXT,
    updated_at TIMESTAMP
);
