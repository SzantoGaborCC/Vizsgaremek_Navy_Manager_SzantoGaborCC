CREATE TABLE public.officer (
	id BIGSERIAL PRIMARY KEY NOT NULL,
	"name" VARCHAR(255) NOT NULL,
	date_of_birth DATE NOT NULL,
	rank_id BIGINT NOT NULL,
	country_id BIGINT NOT NULL
);