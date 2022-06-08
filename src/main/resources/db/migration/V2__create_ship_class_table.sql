CREATE TABLE ship_class (
	id BIGSERIAL PRIMARY KEY NOT NULL,
	"name" VARCHAR(100) NOT NULL,
	displacement_in_tons INTEGER NOT NULL,
	hull_classification_id BIGINT NOT NULL,
	armor_belt_in_cms INTEGER NOT NULL,
	armor_turret_in_cms INTEGER NOT NULL,
	armor_deck_in_cms INTEGER NOT NULL,
	speed_in_kmh INTEGER NOT NULL,
	country_id BIGINT NOT NULL
);