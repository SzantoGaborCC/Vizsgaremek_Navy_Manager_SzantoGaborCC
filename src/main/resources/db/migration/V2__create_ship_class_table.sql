CREATE TABLE ship_class (
	id BIGSERIAL PRIMARY KEY NOT NULL,
	"name" VARCHAR(255) NOT NULL,
	displacement_in_tons INTEGER NOT NULL CHECK (displacement_in_tons > 0),
	hull_classification_id BIGINT NOT NULL,
	armor_belt_in_cms INTEGER NOT NULL CHECK (armor_belt_in_cms > 0),
	armor_turret_in_cms INTEGER NOT NULL CHECK (armor_turret_in_cms > 0),
	armor_deck_in_cms INTEGER NOT NULL CHECK (armor_deck_in_cms > 0),
	speed_in_kmh INTEGER NOT NULL CHECK (speed_in_kmh > 0),
	country_id BIGINT NOT NULL,
    CONSTRAINT each_country_unique_ship_class_name UNIQUE ("name", country_id)
);