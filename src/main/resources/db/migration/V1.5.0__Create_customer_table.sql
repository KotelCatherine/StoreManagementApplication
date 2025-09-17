CREATE TABLE IF NOT EXISTS customers
(
    id UUID PRIMARY KEY,                                   -- Уникальный идентификатор клиента
    first_name        VARCHAR(50)         NOT NULL,        -- Имя клиента (обязательное поле)
    last_name         VARCHAR(50)         NOT NULL,        -- Фамилия клиента (обязательное поле)
    email             VARCHAR(100) UNIQUE NOT NULL,        -- Email клиента (уникальный, обязательный)
    phone             VARCHAR(20),                         -- Телефон клиента
    address           TEXT,                                -- Адрес клиента
    city              VARCHAR(50),                         -- Город клиента
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Дата регистрации
    is_active         BOOLEAN   DEFAULT true,              -- Активен ли клиент
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP
);
