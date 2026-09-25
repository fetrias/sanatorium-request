CREATE SCHEMA IF NOT EXISTS sanatorium;

CREATE TYPE sanatorium.request_status AS ENUM (
    'CREATED',
    'UNDER_REVIEW',
    'APPROVED',
    'REJECTED',
    'CANCELLED'
);

CREATE TABLE IF NOT EXISTS sanatorium.users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    birth_date DATE NOT NULL,
    phone VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS sanatorium.requests (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES sanatorium.users(id),
    sanatorium_name VARCHAR(100) NOT NULL,
    check_in_date DATE NOT NULL,
    duration_days INTEGER NOT NULL CHECK (duration_days > 0),
    create_date DATE NOT NULL,
    status sanatorium.request_status NOT NULL
);
