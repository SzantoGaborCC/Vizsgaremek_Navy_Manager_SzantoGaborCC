CREATE TABLE public."rank" (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    precedence INTEGER UNIQUE NOT NULL,
	designation VARCHAR(100) UNIQUE NOT NULL
);
ALTER TABLE public.officer ADD CONSTRAINT officer_rank_fk FOREIGN KEY (rank_id) REFERENCES public."rank"(id) ;
ALTER TABLE public.hull_classification ADD CONSTRAINT hull_classification_minimum_rank_precedence_fk
    FOREIGN KEY (minimum_rank_id) REFERENCES public."rank"(id);