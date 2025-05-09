-- Drop join tables first
DROP TABLE IF EXISTS user_roles CASCADE;

DROP TABLE IF EXISTS preference_favorite_stores CASCADE;

DROP TABLE IF EXISTS preference_shopping_list CASCADE;

-- Drop dependent tables
DROP TABLE IF EXISTS store_product CASCADE;

DROP TABLE IF EXISTS product_search CASCADE;

DROP TABLE IF EXISTS rating CASCADE;

DROP TABLE IF EXISTS inconsistency CASCADE;

DROP TABLE IF EXISTS wish CASCADE;

-- Drop tables with foreign key dependencies
DROP TABLE IF EXISTS preference CASCADE;

DROP TABLE IF EXISTS customer CASCADE;

DROP TABLE IF EXISTS store CASCADE;

DROP TABLE IF EXISTS product CASCADE;

DROP TABLE IF EXISTS product_category CASCADE;

DROP TABLE IF EXISTS product_brand CASCADE;

DROP TABLE IF EXISTS inconsistency_status CASCADE;

DROP TABLE IF EXISTS user_entity CASCADE;

DROP TABLE IF EXISTS role CASCADE;

DROP TABLE IF EXISTS profeco_admin CASCADE;

DROP TABLE IF EXISTS store_admin CASCADE;