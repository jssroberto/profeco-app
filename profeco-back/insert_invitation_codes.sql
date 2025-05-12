-- Insert 10 invitation codes for PROFECO_ADMIN
INSERT INTO
    public.invitation_code (
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
        gen_random_uuid (),
        encode(sha256(random()::text::bytea), 'hex'),
        NOW(),
        '2026-05-11 00:00:00',
        false,
        '2a8a90da-de71-4a20-aed2-961717032d0e',
        NULL
    ),
    (
        gen_random_uuid (),
        encode(sha256(random()::text::bytea), 'hex'),
        NOW(),
        '2026-05-11 00:00:00',
        false,
        '2a8a90da-de71-4a20-aed2-961717032d0e',
        NULL
    ),
    (
        gen_random_uuid (),
        encode(sha256(random()::text::bytea), 'hex'),
        NOW(),
        '2026-05-11 00:00:00',
        false,
        '2a8a90da-de71-4a20-aed2-961717032d0e',
        NULL
    ),
    (
        gen_random_uuid (),
        encode(sha256(random()::text::bytea), 'hex'),
        NOW(),
        '2026-05-11 00:00:00',
        false,
        '2a8a90da-de71-4a20-aed2-961717032d0e',
        NULL
    ),
    (
        gen_random_uuid (),
        encode(sha256(random()::text::bytea), 'hex'),
        NOW(),
        '2026-05-11 00:00:00',
        false,
        '2a8a90da-de71-4a20-aed2-961717032d0e',
        NULL
    ),
    (
        gen_random_uuid (),
        encode(sha256(random()::text::bytea), 'hex'),
        NOW(),
        '2026-05-11 00:00:00',
        false,
        '2a8a90da-de71-4a20-aed2-961717032d0e',
        NULL
    ),
    (
        gen_random_uuid (),
        encode(sha256(random()::text::bytea), 'hex'),
        NOW(),
        '2026-05-11 00:00:00',
        false,
        '2a8a90da-de71-4a20-aed2-961717032d0e',
        NULL
    ),
    (
        gen_random_uuid (),
        encode(sha256(random()::text::bytea), 'hex'),
        NOW(),
        '2026-05-11 00:00:00',
        false,
        '2a8a90da-de71-4a20-aed2-961717032d0e',
        NULL
    ),
    (
        gen_random_uuid (),
        encode(sha256(random()::text::bytea), 'hex'),
        NOW(),
        '2026-05-11 00:00:00',
        false,
        '2a8a90da-de71-4a20-aed2-961717032d0e',
        NULL
    ),
    (
        gen_random_uuid (),
        encode(sha256(random()::text::bytea), 'hex'),
        NOW(),
        '2026-05-11 00:00:00',
        false,
        '2a8a90da-de71-4a20-aed2-961717032d0e',
        NULL
    );

-- Insert 10 invitation codes for STORE_ADMIN
INSERT INTO
    public.invitation_code (
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
        gen_random_uuid (),
        encode(sha256(random()::text::bytea), 'hex'),
        NOW(),
        '2026-05-11 00:00:00',
        false,
        'b8ac9076-5651-40ef-9687-ac9770224cd5',
        NULL
    ),
    (
        gen_random_uuid (),
        encode(sha256(random()::text::bytea), 'hex'),
        NOW(),
        '2026-05-11 00:00:00',
        false,
        'b8ac9076-5651-40ef-9687-ac9770224cd5',
        NULL
    ),
    (
        gen_random_uuid (),
        encode(sha256(random()::text::bytea), 'hex'),
        NOW(),
        '2026-05-11 00:00:00',
        false,
        'b8ac9076-5651-40ef-9687-ac9770224cd5',
        NULL
    ),
    (
        gen_random_uuid (),
        encode(sha256(random()::text::bytea), 'hex'),
        NOW(),
        '2026-05-11 00:00:00',
        false,
        'b8ac9076-5651-40ef-9687-ac9770224cd5',
        NULL
    ),
    (
        gen_random_uuid (),
        encode(sha256(random()::text::bytea), 'hex'),
        NOW(),
        '2026-05-11 00:00:00',
        false,
        'b8ac9076-5651-40ef-9687-ac9770224cd5',
        NULL
    ),
    (
        gen_random_uuid (),
        encode(sha256(random()::text::bytea), 'hex'),
        NOW(),
        '2026-05-11 00:00:00',
        false,
        'b8ac9076-5651-40ef-9687-ac9770224cd5',
        NULL
    ),
    (
        gen_random_uuid (),
        encode(sha256(random()::text::bytea), 'hex'),
        NOW(),
        '2026-05-11 00:00:00',
        false,
        'b8ac9076-5651-40ef-9687-ac9770224cd5',
        NULL
    ),
    (
        gen_random_uuid (),
        encode(sha256(random()::text::bytea), 'hex'),
        NOW(),
        '2026-05-11 00:00:00',
        false,
        'b8ac9076-5651-40ef-9687-ac9770224cd5',
        NULL
    ),
    (
        gen_random_uuid (),
        encode(sha256(random()::text::bytea), 'hex'),
        NOW(),
        '2026-05-11 00:00:00',
        false,
        'b8ac9076-5651-40ef-9687-ac9770224cd5',
        NULL
    ),
    (
        gen_random_uuid (),
        encode(sha256(random()::text::bytea), 'hex'),
        NOW(),
        '2026-05-11 00:00:00',
        false,
        'b8ac9076-5651-40ef-9687-ac9770224cd5',
        NULL
    );