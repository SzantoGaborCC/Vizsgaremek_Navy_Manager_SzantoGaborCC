CREATE TABLE public."rank" (
	id BIGSERIAL PRIMARY KEY NOT NULL,
	designation VARCHAR(100) NOT NULL,
	precedence BIGINT UNIQUE NOT NULL
);
ALTER TABLE public.officer ADD CONSTRAINT officer_rank_fk FOREIGN KEY (rank_id) REFERENCES public."rank"(id);
ALTER TABLE public.hull_classification ADD CONSTRAINT hull_classification_minimum_rank_precedence_fk
    FOREIGN KEY (minimum_rank_precedence) REFERENCES rank(precedence);