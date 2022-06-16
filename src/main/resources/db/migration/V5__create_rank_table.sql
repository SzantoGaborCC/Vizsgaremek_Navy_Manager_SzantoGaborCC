CREATE TABLE public."rank" (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    precedence INTEGER UNIQUE NOT NULL,
	designation VARCHAR(255) UNIQUE NOT NULL CHECK (LENGTH(designation) > 0)
);
ALTER TABLE public.officer ADD CONSTRAINT officer_rank_fk FOREIGN KEY (rank_id) REFERENCES public."rank"(id) ;
ALTER TABLE public.hull_classification ADD CONSTRAINT hull_classification_minimum_rank_fk
    FOREIGN KEY (minimum_rank_id) REFERENCES public."rank"(id);