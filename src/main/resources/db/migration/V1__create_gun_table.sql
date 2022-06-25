CREATE TABLE gun (
	id BIGSERIAL PRIMARY KEY NOT NULL,
	designation VARCHAR(255) NOT NULL,
	caliber_in_mms INTEGER NOT NULL CHECK (caliber_in_mms > 0),
	projectile_weight_in_kgs INTEGER NOT NULL CHECK (projectile_weight_in_kgs > 0),
	range_in_meters INTEGER NOT NULL CHECK (range_in_meters > 0),
	country_id BIGINT NOT NULL,
    UNIQUE (designation, country_id)
);