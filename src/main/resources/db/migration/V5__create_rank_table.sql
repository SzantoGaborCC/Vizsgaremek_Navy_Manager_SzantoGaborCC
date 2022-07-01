CREATE TABLE public."rank" (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    precedence INTEGER CONSTRAINT precedence_must_be_unique UNIQUE NOT NULL,
	designation VARCHAR(255) CONSTRAINT rank_designation_must_be_unique UNIQUE NOT NULL CHECK (LENGTH(designation) > 0)
);
ALTER TABLE public.officer ADD CONSTRAINT officer_rank_fk FOREIGN KEY (rank_id) REFERENCES public."rank"(id) ;