CREATE TABLE public.officer (
	id BIGSERIAL PRIMARY KEY NOT NULL,
	"name" VARCHAR(255) NOT NULL CHECK (LENGTH(officer."name") > 0),
	date_of_birth DATE NOT NULL CHECK (date_of_birth < NOW()),
	rank_id BIGINT NOT NULL,
	country_id BIGINT NOT NULL
);