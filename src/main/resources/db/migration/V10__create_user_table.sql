CREATE TABLE public.user (
	id BIGSERIAL PRIMARY KEY NOT NULL,
	"username" VARCHAR(255) UNIQUE NOT NULL CHECK (LENGTH(username) > 1),
	"password" VARCHAR(255) NULL CHECK (LENGTH(password) > 7)
);