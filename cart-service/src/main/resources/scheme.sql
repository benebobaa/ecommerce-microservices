CREATE TABLE IF NOT EXISTS
    carts (
        id BIGSERIAL PRIMARY KEY,
        status VARCHAR(255) NOT NULL,
        total_price NUMERIC(10, 2) NOT NULL,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS
    cart_items (
        id BIGSERIAL PRIMARY KEY,
        quantity INTEGER NOT NULL,
        product_id BIGINT NOT NULL,
        cart_id BIGINT NOT NULL REFERENCES carts(id) ON DELETE CASCADE
    );