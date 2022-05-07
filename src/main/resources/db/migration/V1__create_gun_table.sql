CREATE TABLE gun (
	id BIGSERIAL PRIMARY KEY NOT NULL,
	designation VARCHAR(100) NOT NULL,
	caliber_in_mms INTEGER NOT NULL,
	projectile_weight_in_kgs INTEGER NOT NULL,
	range_in_meters INTEGER NOT NULL,
	minimum_ship_displacement INTEGER NOT NULL,
	country_id BIGINT NOT NULL
);