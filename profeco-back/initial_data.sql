-- Roles
INSERT INTO
    role (id, name)
VALUES
    (
        'a1b2c3d4-e5f6-7777-8888-999000000001',
        'CUSTOMER'
    ),
    (
        'a1b2c3d4-e5f6-7777-8888-999000000002',
        'STORE_ADMIN'
    ),
    (
        'a1b2c3d4-e5f6-7777-8888-999000000003',
        'PROFECO_ADMIN'
    );

-- Inconsistency Statuses
INSERT INTO
    inconsistency_status (id, name)
VALUES
    ('5a8f7961-0357-4733-8576-19970a690001', 'OPEN'),
    ('5a8f7961-0357-4733-8576-19970a690002', 'CLOSED');

-- Product Brands
INSERT INTO
    product_brand (id, name)
VALUES
    ('b2c8a3f5-1029-4e4a-8e21-32f89a0b0001', 'Lala'),
    (
        'b2c8a3f5-1029-4e4a-8e21-32f89a0b0002',
        'Coca-Cola'
    ),
    (
        'b2c8a3f5-1029-4e4a-8e21-32f89a0b0003',
        'Sabritas'
    ),
    ('b2c8a3f5-1029-4e4a-8e21-32f89a0b0004', 'Bimbo'),
    ('b2c8a3f5-1029-4e4a-8e21-32f89a0b0005', 'Nestle');

-- Product Categories
INSERT INTO
    product_category (id, name)
VALUES
    ('c3d9b4e6-2130-4f5b-9f32-43a90b1c0001', 'Lácteos'),
    ('c3d9b4e6-2130-4f5b-9f32-43a90b1c0002', 'Bebidas'),
    ('c3d9b4e6-2130-4f5b-9f32-43a90b1c0003', 'Snacks'),
    (
        'c3d9b4e6-2130-4f5b-9f32-43a90b1c0004',
        'Panadería'
    ),
    (
        'c3d9b4e6-2130-4f5b-9f32-43a90b1c0005',
        'Despensa'
    );

-- Stores (Existing UUIDs are valid, no changes needed for these IDs)
INSERT INTO
    store (id, image_url, location, name)
VALUES
    (
        'f1a2b3c4-d5e6-5555-6666-777000000001',
        'localhost:8080/uploads/images/84d5f6c8-a192-4436-80c6-7bf9b834ceb5.jpg',
        'Calle Principal 123, Colonia Centro, Ciudad de México',
        'Walmart'
    ),
    (
        'f1a2b3c4-d5e6-5555-6666-777000000002',
        'localhost:8080/uploads/images/ccc0eac5-f0a2-47dc-8d71-236e41d0f064.jpeg',
        'Avenida Roble 456, Colonia del Valle, Monterrey',
        'Soriana'
    ),
    (
        'f1a2b3c4-d5e6-5555-6666-777000000003',
        'localhost:8080/uploads/images/1aa83b43-93e2-4287-b148-0e8ce5a5fe01.webp',
        'Boulevard del Pino 789, Colonia Las Arboledas, Guadalajara',
        'Chedraui'
    );

-- User Entities (Existing UUIDs are valid, no changes needed for these IDs)
INSERT INTO
    user_entity (id, email, password)
VALUES
    (
        'b1c2d3e4-f5a6-1111-2222-333000000001',
        'customer1@example.com',
        '$2a$10$gXbtPwts1fNV5D/YJnecxOSLLCX.qJN4Z1VXBAVlsNZut2A8U4xeC'
    ),
    (
        'b1c2d3e4-f5a6-1111-2222-333000000002',
        'storeadmin1@example.com',
        '$2a$10$gXbtPwts1fNV5D/YJnecxOSLLCX.qJN4Z1VXBAVlsNZut2A8U4xeC'
    ),
    (
        'b1c2d3e4-f5a6-1111-2222-333000000003',
        'profecoadmin1@example.com',
        '$2a$10$gXbtPwts1fNV5D/YJnecxOSLLCX.qJN4Z1VXBAVlsNZut2A8U4xeC'
    ),
    (
        'b1c2d3e4-f5a6-1111-2222-333000000004',
        'customer2@example.com',
        '$2a$10$gXbtPwts1fNV5D/YJnecxOSLLCX.qJN4Z1VXBAVlsNZut2A8U4xeC'
    );

-- User Roles (Existing UUIDs are valid, no changes needed for these IDs)
INSERT INTO
    user_roles (user_id, role_id)
VALUES
    (
        'b1c2d3e4-f5a6-1111-2222-333000000001',
        'a1b2c3d4-e5f6-7777-8888-999000000001'
    ), -- customer1 is CUSTOMER
    (
        'b1c2d3e4-f5a6-1111-2222-333000000002',
        'a1b2c3d4-e5f6-7777-8888-999000000002'
    ), -- storeadmin1 is STORE_ADMIN
    (
        'b1c2d3e4-f5a6-1111-2222-333000000003',
        'a1b2c3d4-e5f6-7777-8888-999000000003'
    ), -- profecoadmin1 is PROFECO_ADMIN
    (
        'b1c2d3e4-f5a6-1111-2222-333000000004',
        'a1b2c3d4-e5f6-7777-8888-999000000001'
    );

-- customer2 is CUSTOMER
-- Customers (Existing UUIDs are valid, no changes needed for these IDs)
INSERT INTO
    customer (id, name, user_id)
VALUES
    (
        'c1d2e3f4-a5b6-2222-3333-444000000001',
        'Alice Wonderland',
        'b1c2d3e4-f5a6-1111-2222-333000000001'
    ),
    (
        'c1d2e3f4-a5b6-2222-3333-444000000002',
        'Bob The Builder',
        'b1c2d3e4-f5a6-1111-2222-333000000004'
    );

-- Profeco Admins (Existing UUIDs are valid, no changes needed for these IDs)
INSERT INTO
    profeco_admin (id, name, user_id)
VALUES
    (
        'e1f2a3b4-c5d6-4444-5555-666000000001',
        'Carlos Profeco',
        'b1c2d3e4-f5a6-1111-2222-333000000003'
    );

-- Store Admins (Existing UUIDs are valid, no changes needed for these IDs)
INSERT INTO
    store_admin (id, name, store_id, user_id)
VALUES
    (
        'd1e2f3a4-b5c6-3333-4444-555000000001',
        'Steve Walmart',
        'f1a2b3c4-d5e6-5555-6666-777000000001',
        'b1c2d3e4-f5a6-1111-2222-333000000002'
    );

-- Products
INSERT INTO
    product (id, image_url, name, brand_id, category_id)
VALUES
    (
        'd4ea0c57-3241-406c-a043-54ba1c2d0001',
        'localhost:8080/uploads/images/15899148-88c6-4da3-b60c-1b282323fef9.webp',
        'Leche Entera 1L',
        'b2c8a3f5-1029-4e4a-8e21-32f89a0b0001', -- Lala
        'c3d9b4e6-2130-4f5b-9f32-43a90b1c0001' -- Lácteos
    ),
    (
        'd4ea0c57-3241-406c-a043-54ba1c2d0002',
        'localhost:8080/uploads/images/587e90f9-cb65-414e-8102-3780c8fea200.webp',
        'Coca-Cola 600ml',
        'b2c8a3f5-1029-4e4a-8e21-32f89a0b0002', -- Coca-Cola
        'c3d9b4e6-2130-4f5b-9f32-43a90b1c0002' -- Bebidas
    ),
    (
        'd4ea0c57-3241-406c-a043-54ba1c2d0003',
        'localhost:8080/uploads/images/f5df5c38-7f0a-4178-b944-ea6e63289d30.webp',
        'Sabritas Original 170g',
        'b2c8a3f5-1029-4e4a-8e21-32f89a0b0003', -- Sabritas
        'c3d9b4e6-2130-4f5b-9f32-43a90b1c0003' -- Snacks
    ),
    (
        'd4ea0c57-3241-406c-a043-54ba1c2d0004',
        'localhost:8080/uploads/images/9782cf14-b9c5-4f5b-906e-e3f6cb422805.webp',
        'Pan Blanco Bimbo',
        'b2c8a3f5-1029-4e4a-8e21-32f89a0b0004', -- Bimbo
        'c3d9b4e6-2130-4f5b-9f32-43a90b1c0004' -- Panadería
    ),
    (
        'd4ea0c57-3241-406c-a043-54ba1c2d0005',
        'localhost:8080/uploads/images/c44e3527-625f-4109-a606-5de3deb97b96.webp',
        'Leche Evaporada Nestle',
        'b2c8a3f5-1029-4e4a-8e21-32f89a0b0005', -- Nestle
        'c3d9b4e6-2130-4f5b-9f32-43a90b1c0001' -- Lácteos
    );

-- Store Products
INSERT INTO
    store_product (
        id,
        price,
        offer_price,
        offer_start_date,
        offer_end_date,
        product_id,
        store_id,
        inconsistency_id
    )
VALUES
    (
        'e5fb1d68-4352-417d-b154-65cb2d3e0001',
        25.50,
        22.00,
        '2025-05-10',
        '2025-05-20',
        'd4ea0c57-3241-406c-a043-54ba1c2d0001', -- Leche Entera 1L
        'f1a2b3c4-d5e6-5555-6666-777000000001',
        NULL
    ),
    (
        'e5fb1d68-4352-417d-b154-65cb2d3e0002',
        15.00,
        NULL,
        NULL,
        NULL,
        'd4ea0c57-3241-406c-a043-54ba1c2d0002', -- Coca-Cola 600ml
        'f1a2b3c4-d5e6-5555-6666-777000000001',
        NULL
    ),
    (
        'e5fb1d68-4352-417d-b154-65cb2d3e0003',
        18.75,
        17.00,
        '2025-05-15',
        '2025-05-25',
        'd4ea0c57-3241-406c-a043-54ba1c2d0003', -- Sabritas Original 170g
        'f1a2b3c4-d5e6-5555-6666-777000000002',
        NULL
    ),
    (
        'e5fb1d68-4352-417d-b154-65cb2d3e0004',
        30.00,
        NULL,
        NULL,
        NULL,
        'd4ea0c57-3241-406c-a043-54ba1c2d0004', -- Pan Blanco Bimbo
        'f1a2b3c4-d5e6-5555-6666-777000000001',
        NULL
    ),
    (
        'e5fb1d68-4352-417d-b154-65cb2d3e0005',
        12.50,
        10.00,
        '2025-05-01',
        '2025-05-30',
        'd4ea0c57-3241-406c-a043-54ba1c2d0002', -- Coca-Cola 600ml
        'f1a2b3c4-d5e6-5555-6666-777000000002',
        NULL
    );

-- Inconsistencies
INSERT INTO
    inconsistency (
        id,
        actual_price,
        date_time,
        published_price,
        customer_id,
        status_id,
        store_product_id
    )
VALUES
    (
        'f60c2e79-5463-428e-c265-76dc3e4f0001',
        23.00,
        '2025-05-14 10:00:00',
        22.00,
        'c1d2e3f4-a5b6-2222-3333-444000000001',
        '5a8f7961-0357-4733-8576-19970a690001', -- OPEN status
        'e5fb1d68-4352-417d-b154-65cb2d3e0001' -- Store Product 1
    );

-- Ratings
INSERT INTO
    rating (id, comment, date, score, user_id, store_id)
VALUES
    (
        '171d3f8a-6574-439f-d376-87ed4f500001',
        'Great prices and selection!',
        '2025-05-12',
        5,
        'c1d2e3f4-a5b6-2222-3333-444000000001',
        'f1a2b3c4-d5e6-5555-6666-777000000001'
    ),
    (
        '171d3f8a-6574-439f-d376-87ed4f500002',
        'A bit crowded but good offers.',
        '2025-05-13',
        4,
        'c1d2e3f4-a5b6-2222-3333-444000000002',
        'f1a2b3c4-d5e6-5555-6666-777000000002'
    );

-- Wishes
INSERT INTO
    wish (id, date, description, customer_id, store_id)
VALUES
    (
        '282e409b-7685-44a0-e487-98fe50610001',
        '2025-05-15',
        'More organic vegetable options',
        'c1d2e3f4-a5b6-2222-3333-444000000001',
        'f1a2b3c4-d5e6-5555-6666-777000000001'
    ),
    (
        '282e409b-7685-44a0-e487-98fe50610002',
        '2025-05-14',
        'Please stock lactose-free Lala milk',
        'c1d2e3f4-a5b6-2222-3333-444000000002',
        'f1a2b3c4-d5e6-5555-6666-777000000002'
    );

-- Preferences
INSERT INTO
    preference (id, customer_id)
VALUES
    (
        '393f51ab-8796-45b1-f598-a90f61720001',
        'c1d2e3f4-a5b6-2222-3333-444000000001'
    ),
    (
        '393f51ab-8796-45b1-f598-a90f61720002',
        'c1d2e3f4-a5b6-2222-3333-444000000002'
    );

-- Preference Favorite Stores
INSERT INTO
    preference_favorite_stores (preference_id, store_id)
VALUES
    (
        '393f51ab-8796-45b1-f598-a90f61720001', -- Preference 1
        'f1a2b3c4-d5e6-5555-6666-777000000001'
    ),
    (
        '393f51ab-8796-45b1-f598-a90f61720002', -- Preference 2
        'f1a2b3c4-d5e6-5555-6666-777000000002'
    );

-- Preference Shopping List
INSERT INTO
    preference_shopping_list (preference_id, product_id)
VALUES
    (
        '393f51ab-8796-45b1-f598-a90f61720001', -- Preference 1
        'd4ea0c57-3241-406c-a043-54ba1c2d0001' -- Leche Entera 1L
    ),
    (
        '393f51ab-8796-45b1-f598-a90f61720001', -- Preference 1
        'd4ea0c57-3241-406c-a043-54ba1c2d0002' -- Coca-Cola 600ml
    ),
    (
        '393f51ab-8796-45b1-f598-a90f61720002', -- Preference 2
        'd4ea0c57-3241-406c-a043-54ba1c2d0003' -- Sabritas Original 170g
    );

-- Product Searches (by preference)
INSERT INTO
    product_search (id, search_count, preference_id, product_id)
VALUES
    (
        '4a4062bc-98a7-46c2-06a9-ba1072830001',
        5,
        '393f51ab-8796-45b1-f598-a90f61720001', -- Preference 1
        'd4ea0c57-3241-406c-a043-54ba1c2d0001' -- Leche Entera 1L
    ),
    (
        '4a4062bc-98a7-46c2-06a9-ba1072830002',
        3,
        '393f51ab-8796-45b1-f598-a90f61720002', -- Preference 2
        'd4ea0c57-3241-406c-a043-54ba1c2d0003' -- Sabritas Original 170g
    );

-- Invitation Codes
INSERT INTO
    invitation_code (
        id,
        code,
        created_at,
        expiration_date,
        used,
        role_id,
        used_by_user_id
    )
VALUES
    (
        '5b5173cd-a9b8-47d3-17ba-cb2183940001',
        'STOREADMINCODE1',
        '2025-05-01 00:00:00',
        '2025-06-01 00:00:00',
        FALSE,
        'a1b2c3d4-e5f6-7777-8888-999000000002',
        NULL
    ),
    (
        '5b5173cd-a9b8-47d3-17ba-cb2183940002',
        'PROFECOADMINCODE1',
        '2025-05-01 00:00:00',
        '2025-06-01 00:00:00',
        FALSE,
        'a1b2c3d4-e5f6-7777-8888-999000000003',
        NULL
    ),
    (
        '5b5173cd-a9b8-47d3-17ba-cb2183940003',
        'USEDSTORECODE',
        '2025-04-01 00:00:00',
        '2025-05-01 00:00:00',
        TRUE,
        'a1b2c3d4-e5f6-7777-8888-999000000002',
        'b1c2d3e4-f5a6-1111-2222-333000000002'
    );